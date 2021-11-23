import * as builder from 'generator-builder';
import _exampleJS from './generators/example/example.js';
import _exampleTS from '../ts-out/generators/example/index.js';

export const testing = builder.testingGenerator;
export const exampleScala = builder.exampleGenerator;
export const exampleJS = _exampleJS;
export const exampleTS = _exampleTS;