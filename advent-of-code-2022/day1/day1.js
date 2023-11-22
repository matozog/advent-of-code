const fs = require("fs");

const fileName = 'input1.txt';

const elvesMap = [];
let maxSum = 0;
let elfMaxSumIndex = 0;

fs.readFile(fileName, (err, data) => {
    if (err) throw err;

    const numberArray = data.toString().split(/\r?\n/);
    let elfIndex = 1;
    let elfCaloriesSum = 0;

    for (let i = 0; i < numberArray.length; i++) {
        if (numberArray[i] !== '') {
            elfCaloriesSum += parseInt(numberArray[i]);
        }
        else {
            if (maxSum < elfCaloriesSum) {
                maxSum = elfCaloriesSum;
                elfMaxSumIndex = elfIndex;
            }
            elvesMap.push(elfCaloriesSum);
            elfCaloriesSum = 0;
            elfIndex++;
        }
    }

    console.log(maxSum, elfMaxSumIndex);


    elvesMap.sort((a, b) => b - a);

    console.log(elvesMap);

    console.log(elvesMap[0] + elvesMap[1] + elvesMap[2]);
})

