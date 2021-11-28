declare module 'generator-builder' {

export class Generator {
  constructor (
    id: string,
    name: string,
    thumbnail: void,
    video: void,
    instructions: string,
    pageAmount: number,
    setup: ((generator: Generator) => void),
    change: ((generator: Generator) => void),
    inputs: Input<any>[],
  );

  public readonly id: string;
  public readonly name: string;
  public readonly thumbnail: void;
  public readonly video: void;
  public readonly instructions: string;
  public readonly pageAmount: number;
  public readonly inputs: Input<any>[];

  public readonly pages: Page[];

  public runSetup(generator: Generator): void;
  public onChange(generator: Generator): void;
}

export class Image {
  constructor(
    url: string,
  );

  public readonly url: string;

  // public transform(transformation: Transformation): Image;

  /**
     * Crop the image
     * @param startX x coordinate of the top-left point
     * @param startY y coordinate of the top-left point
     * @param endX x coordinate of the bottom-right point
     * @param endY y coordinate of the bottom-right point
     */
  public crop(startX: number, startY: number, endX: number, endY: number): Image;

  /**
     * Rotate the image around the chosen origin point
     * @param angle Angle in degrees, counterclockwise, of rotation
     * @param origin Whether the origin should be (0, 0) or the image center
     */
  public rotate(angle: number, origin: "top left" | "center"): Image;

  /**
     * Rotate the image around the given rotational origin
     * @param angle Angle in degrees, counterclockwise, of rotation
     * @param originX x coordinate of the rotational origin
     * @param originY y coordinate of the rotational origin
     */
  public rotate(angle: number, originX: number, originY: number): Image;

  /**
     * Scale the image by a factor of `factorX`*`factorY`
     */
  public scale(factorX: number, factorY: number, algorithm?: ScaleAlgorithm): Image;

  public scaleTo(width: number, height: number, algorithm?: ScaleAlgorithm): Image;

  /**
     * Flip the image on the x axis
     */
  public get flipVertical(): Image;

  /**
     * Flip the image on the y axis
     */
  public get flipHorizontal(): Image;

  /**
     * Blends the image with a layer of the provided RGB color
     * @usecase This works the same way Minecraft colorizes grayscale textures, so getting colors from there should give you correct results
     */
  public blend(r: number, g: number, b: number): Image;
}

export enum ScaleAlgorithm {
  NearestNeighbor,
  Classic,
}

export class Page {
  constructor(generator: Generator, idx: number);

  /**
     * Draws an image at the given origin
     * @param x x coordinate of where the image's origin will end up
     * @param y y coordinate of where the image's origin will end up
     */
  public draw(image: Image, x: number, y: number): void;
}

export const PageSizes: {
  A4: {
    px: {
      width: number,
      height: number,
    },
  },
}

export class Texture extends Image {
  constructor(url?: string);
}

class Input<A> {
  public create(): string;
  public read(): A;
}

export class BooleanInput extends Input<boolean> {
  constructor(label: string, def: boolean);
}

export class ButtonInput extends Input<void> {
  constructor(label: string, callback: () => void);
}

export class SelectInput extends Input<string> {
  constructor(label: string, options: string[]);
}

export class Text extends Input<never> {
  constructor(text: string);
}

export class TextureInput extends Input<Promise<Texture>> {
  constructor(label: string, width: number, height: number, choices: Texture[]);
}

export class NumberInput extends Input<number> {
  constructor(label: string, min: number, max: number, step: number);
}

export class RangeInput extends Input<number> {
  constructor(label: string, min: number, max: number, step: number);
}

export class TextInput extends Input<string> {
  constructor(label: string, def: string);
}

export class ColorInput extends Input<[number, number, number]> {
  constructor(label: string, defaultRed: number, defaultGreen: number, defaultBlue: number);
}

}