import * as builder from 'generator-builder'

import backgroundImage from './images/Background.png';
import foldsImage from './images/Folds.png';

const background = builder.Image(backgroundImage), folds = builder.Image(foldsImage)

const showFolds = builder.BooleanInput("Show Folds", true)
const skin = builder.TextureInput("Skin", 64, 64, [builder.Texture()])

function setup(generator) {
  generator.pages[0].draw(background, 0, 0)
}

function change(generator) {
  skin.read().then(img => {
    drawHead(generator, img, 185, 117);
    if (showFolds.read())
      generator.pages[0].draw(folds, 0, 0);
  });
}

function drawHead(generator, skin, x, y) {
  function drawRect(src1, src2, src3, src4, dst1, dst2, dst3, dst4, flip) {
    const img = skin
      .crop(src1, src2, src1 + src3, src2 + src4)
      .scale(dst3 / src3, dst4 / src4)
    generator.pages[0].draw(flip ? img.flipVertical : img, dst1, dst2)
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

export default builder.Generator(
  "exampleJS",
  "Example Generator (JavaScript)",
  undefined,
  undefined,
  "",
  1,
  setup,
  change,
  [skin, showFolds]
)