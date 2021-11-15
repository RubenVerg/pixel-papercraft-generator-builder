import * as path from 'path';
import { fileURLToPath } from 'url';
import HtmlWebpackPlugin from 'html-webpack-plugin';

const port = process.env.PORT || 3000;

let isProduction = process.env.NODE_ENV === "production";
const mode = isProduction ? "production" : "development";
const devtool = isProduction ? "source-map" : "inline-source-map";

const __dirname = fileURLToPath(path.dirname(import.meta.url));

export default {
  mode,
  entry: "./src/test/js/index.js",
  devtool,
  devServer: {
    static: "./dist",
    port,
  },
  plugins: [new HtmlWebpackPlugin({ template: "templates/index.html" })],
  output: {
    filename: "[name].bundle.js",
    path: path.resolve(__dirname, "dist"),
    clean: true,
  },
  module: {
    rules: [
      {
        test: /\.(js)$/,
        exclude: /node_modules/,
        use: [{
          loader: "babel-loader",
          options: {
            presets: ['@babel/preset-env']
          }
        }]
      },
      {
        test: /\.png|\.jpg|\.jpeg/,
        type: "asset/resource",
      },
      {
        test: /\.css$/,
        use: ["style-loader", "css-loader", "postcss-loader"],
      },
    ],
  },
  resolve: {
    alias: {
      'generator-builder': path.resolve(__dirname, `./target/scala-3.1.0/generator-builder${isProduction ? "-test-opt" : "-test-fastopt"}/main.js`)
    },
  },
};
