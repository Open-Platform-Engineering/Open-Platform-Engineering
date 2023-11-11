const HtmlWebpackPlugin = require('html-webpack-plugin');
const path = require('path');

module.exports = {
  entry: path.resolve(__dirname, 'src', 'index.js'), // As the index.jsx is transpiled already it is a .js file now
  mode: 'development',
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: 'bundle.js',
  },
 optimization: {
     usedExports: true,
   },
  module: {
    rules: [
      {
        test: /\.css$/i,
        include: path.resolve(__dirname, 'src'),
        exclude: /(node_modules)/,
        use: ['style-loader', 'css-loader', 'postcss-loader'],
      },
      {
        test: /\.(png|jpe?g|gif)$/i,
        loader: 'file-loader',
        options: {
          outputPath: 'images',
        },
      },
    ],
  },
  // No need of SWC as it has been ran outside of Webpack during a Bazel build
  plugins: [
    new HtmlWebpackPlugin({
      filename: './index.html',
      template: path.join(__dirname, 'public/index.html'),
    }),
  ],
  devServer: {
    historyApiFallback: true,
    setupMiddlewares: (middlewares,devServer) => {
      if (!devServer) {
        throw new Error('webpack-dev-server is not defined');
      }

//      devServer.app.get('/_aws_config', (_, res) => {
//        res.json({
//          Region: "",
//          UserPoolId: "",
//          ClientId: "",
//          IdentityPoolId: ""
//        });
//      });
      return middlewares;
    }
  },
  resolve: {
    extensions: ['.js']
  },
};
