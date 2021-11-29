import { loadTexture, RangeInput, Generator } from 'generator-builder';

import skinImage from './images/Skin.png';

const skin = loadTexture(skinImage);

const input = new RangeInput("Angle φ (degrees)", 0, 360, 1)

const setup = (generator: Generator) => { }

const change = async (generator: Generator) => {
  generator.pages[0].draw((await skin).crop(8, 8, 16, 16).scaleBy(8, 8).rotate(input.read()), 0, 0)
}

export default new Generator(
  "weee",
  "Weee rotations",
  undefined, undefined,
  "idk man pull the range and see the image spin", 1,
  setup, change, [input]
)