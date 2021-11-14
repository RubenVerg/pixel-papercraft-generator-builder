import * as generatorBuilder from 'generator-builder'
import * as generators from './generators.js'

const generatorName = window.location.hash.slice(1), generator = generators[generatorName]
console.log(generatorName, generator, generators)

generatorBuilder.BarebonesRenderer(generator)