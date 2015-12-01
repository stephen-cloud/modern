'use strict';

angular.module('modernApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


