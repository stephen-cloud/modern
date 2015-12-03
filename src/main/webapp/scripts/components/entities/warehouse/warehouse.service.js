'use strict';

angular.module('modernApp')
    .factory('Warehouse', function ($resource, DateUtils) {
        return $resource('api/warehouses/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
