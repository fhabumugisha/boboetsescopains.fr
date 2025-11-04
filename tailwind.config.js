/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/main/resources/templates/**/*.html",
    "./src/main/resources/static/js/**/*.js"
  ],
  theme: {
    extend: {
      colors: {
        primary: '#0d6efd',
        secondary: '#6c757d',
      },
    },
  },
  plugins: [],
}
