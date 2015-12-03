'use strict';

angular.module('modernApp')
    .controller('ProductDetailController', function ($scope, $rootScope, $stateParams, entity, Product, Warehouse) {
        $scope.product = entity;
        $scope.load = function (id) {
            Product.get({id: id}, function(result) {
                $scope.product = result;
            });
        };
        var unsubscribe = $rootScope.$on('modernApp:productUpdate', function(event, result) {
            $scope.product = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
