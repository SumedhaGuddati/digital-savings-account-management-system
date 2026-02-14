module.exports = {
  preset: "@vue/cli-plugin-unit-jest/presets/typescript-and-babel",
  moduleNameMapper: {
    "\\.(css|less|scss|sass)$": "identity-obj-proxy",
    '^@/(.*)$': '<rootDir>/src/$1'
  },
  moduleFileExtensions: ["js", "ts", "json", "vue"],
  transform: {
    '^.+\\.vue$': '@vue/vue3-jest',
    '^.+\\.ts$': 'ts-jest',
    '^.+\\.js$': 'babel-jest'
  },
  testEnvironment: 'jsdom',
  globals: {
    'ts-jest': {
      babelConfig: true
    }
  }
};
