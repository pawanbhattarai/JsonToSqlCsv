document.getElementById('sql-convert').addEventListener('click', () => {
    convertJson('Sql');
});

document.getElementById('csv-convert').addEventListener('click', () => {
    convertJson('Csv');
});


async function convertJson(format) {
const jsonInput = document.getElementById('json-input').value;
console.log('JSON Input:', jsonInput); // Log the JSON input
try {
// Convert JSON input string to JSON object
const jsonObject = JSON.parse(jsonInput);

const response = await fetch('http://localhost:8083/convert/' + (format === 'Sql' ? 'toSql' : 'toCsv'), {
method: 'POST',
headers: {
    'Content-Type': 'application/json'
},
body: JSON.stringify(jsonObject)
});

if (response.ok) {
const fileContent = await response.blob();
const downloadLink = document.getElementById('download-link');
downloadLink.href = URL.createObjectURL(fileContent);
downloadLink.download = `output.${format.toLowerCase()}`;
downloadLink.style.display = 'block';
} else {
console.error('Error:', response.statusText);
}
} catch (error) {
console.error('Error:', error.message);
}
}