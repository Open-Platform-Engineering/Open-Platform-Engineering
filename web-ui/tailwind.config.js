const withMT = require("@material-tailwind/react/utils/withMT");

/** @type {import('tailwindcss').Config} */
module.exports = withMT({
  content: [
    "./public/index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
    "../node_modules/@material-tailwind/react/components/**/*.{js,ts,jsx,tsx}",
    "../node_modules/@material-tailwind/react/theme/components/**/*.{js,ts,jsx,tsx}",
    "../node_modules/react-tailwindcss-select/dist/index.esm.js"
  ],
  theme: {
    extend: {
    },
  },
  plugins: [],
});
