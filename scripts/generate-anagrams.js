const fs = require('fs');

function generateRandomWord(length) {
  let result = '';
  const characters = 'abcdefghijklmnopqrstuvwxyz';
  for (let i = 0; i < length; i++) {
    result += characters.charAt(Math.floor(Math.random() * characters.length));
  }
  return result;
}

function shuffleWord(word) {
  return word
    .split('')
    .sort(() => Math.random() - 0.5)
    .join('');
}

function generateTestFile(path, numWords, numAnagrams) {
  const words = new Set();

  while (words.size < numWords - numAnagrams) {
    words.add(generateRandomWord(Math.ceil(Math.random() * 30) + 1));
  }

  const wordsArray = Array.from(words);
  for (let i = 0; i < numAnagrams; i++) {
    const word = wordsArray[Math.floor(Math.random() * wordsArray.length)];
    words.add(shuffleWord(word));
  }

  const stream = fs.createWriteStream(path, { flags: 'a' });
  const sortedWords = Array.from(words).sort((a, b) => a.length - b.length);

  for (const word of sortedWords) {
    stream.write(word + '\n');
  }
  stream.end();
}

generateTestFile('data/input.txt', 10000000, 5000000); // 10 million words, 5 million anagrams