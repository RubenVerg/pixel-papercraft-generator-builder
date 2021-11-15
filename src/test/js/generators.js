import * as builder from 'generator-builder';
import _exampleJS from './generators/example/example.js';

export const testing = builder.testingGenerator;
export const exampleScala = builder.exampleGenerator;
export const exampleJS = _exampleJS;