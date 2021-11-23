import * as builder from 'generator-builder';

import backgroundImage from './images/Background.png';
import foldsImage from './images/Folds.png';

const background = new builder.Image(backgroundImage), folds = new builder.Image(foldsImage)

const showFolds = new builder.BooleanInput('Show Folds', true)
const skin = new builder.TextureInput('Skin', 64, 64, [new builder.Texture()])

const setup = (generator: builder.Generator) => {
  generator.pages[0].draw(background, 0, 0)
}

const change = (generator: builder.Generator) => {
  skin.read().then(img => {
    drawHead(generator, img, 185, 117);
    if (showFolds.read()) generator.pages[0].draw(folds, 0, 0);
  });
}

function drawHead(generator: builder.Generator, skin: builder.Image, x: number, y: number) {
  function drawRect(src1: number, src2: number, src3: number, src4: number, dst1: number, dst2: number, dst3: number, dst4: number, flip: boolean) {
    const img = (skin
      .crop(src1, src2, src1 + src3, src2 + src4)
      .scaleTo(dst3, dst4)
    );
    generator.pages[0].draw(flip ? img.flipVertical : img, dst1, dst2);
  }

  drawRect(0, 8, 8, 8, x - 64, y + 0, 64, 64, false) // Right
  drawRect(8, 8, 8, 8, x + 0, y + 0, 64, 64, false) // Face
  drawRect(16, 8, 8, 8, x + 64, y + 0, 64, 64, false) // Left
  drawRect(24, 8, 8, 8, x + 128, y + 0, 64, 64, false) // Back
  drawRect(8, 0, 8, 8, x + 0, y - 64, 64, 64, false) // Top
  drawRect(16, 0, 8, 8, x + 0, y + 64, 64, 64, true) // Bottom

  drawRect(32, 8, 8, 8, x - 64, y + 0, 64, 64, false) // Right
  drawRect(40, 8, 8, 8, x + 0, y + 0, 64, 64, false) // Face
  drawRect(48, 8, 8, 8, x + 64, y + 0, 64, 64, false) // Left
  drawRect(56, 8, 8, 8, x + 128, y + 0, 64, 64, false) // Back
  drawRect(40, 0, 8, 8, x + 0, y - 64, 64, 64, false) // Top
  drawRect(48, 0, 8, 8, x + 0, y + 64, 64, 64, true) // Bottom
}

export default new builder.Generator(
  "exampleTS",
  "Example Generator (TypeScript)",
  undefined,
  undefined,
  "",
  1,
  setup,
  change,
  [skin, showFolds],
);