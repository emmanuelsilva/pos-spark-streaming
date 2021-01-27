const fs = require('fs');

const configurations = process.argv.slice(2);
const date = configurations[0];
const startHour = configurations[1];

const zeroPad = (value) => {
  if (value < 10) return `0${value}`;
  return value;
}

const generateTransactionId = () => {
  return Math.floor(Math.random() * 100000) + 1;
}

const productRandomLines = (date, hour) => {
  const seconds = Array.from(Array(60).keys())

  return seconds
    .map(second =>
      `'${date} ${hour}:${zeroPad(second)}',${generateTransactionId()},'customer-abc','product-123',100`
    )
    .join('\n');
}

const delay = ms => new Promise(resolve => setTimeout(resolve,ms))

const generateFilePerHour = (date, hour) => {
  const csvContent = productRandomLines(date, `${zeroPad(hour)}:00`);
  const fileName = `/Users/emmanuel.silva/temp-dir/input/${Date.now()}.csv`;
  fs.writeFile(fileName, csvContent, (err) => {});
}

const generateDayFiles = async () => {
  for (let i=startHour; i<=23; i++) {
    console.log(`date ${date} start time ${i}`);
    generateFilePerHour(date, i);
    await delay(1000);
  }
}

generateDayFiles().then(() => console.log("done!"));