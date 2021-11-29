import * as builder from 'generator-builder';
import exampleJS from './generators/example/example.js';
import exampleTS from '../ts-out/generators/example/index.js';
import weee from '../ts-out/generators/weee/index.js';

export default {
  exampleJS, exampleTS, exampleScala: builder.exampleGenerator, testing: builder.testingGenerator, weee
};