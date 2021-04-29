define([],
  function() {
    'use strict';

    class AppConstants {

      addConstant(name, value) {
        Object.defineProperty(this, name, {
          value: value,
          enumerable: true
        });
      }

      constructor() {
        this.addConstant('CONTEXT_PATH', '/treen');
      }

    }

    return new AppConstants();
  });