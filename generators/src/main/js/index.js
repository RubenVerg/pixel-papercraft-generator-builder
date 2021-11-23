import * as generatorBuilder from 'generator-builder';
import * as generators from './generators.js';

const generatorName = new URLSearchParams(window.location.search).get('generator')
if (generatorName) {
  const generator = generators[generatorName];
  console.log(generatorName, generator, generators);

  generatorBuilder.BarebonesRenderer(generator);
} else {
  document.body.innerHTML = `
    <h1>Generators</h1>
    <ul>
    ${Object.entries(generators).map(([id, generator]) => `<li><a href="/?generator=${id}">${generator.name}</a></li>`).join('')}
    </ul>
  `;
}