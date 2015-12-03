'use strict';

angular.module('modernApp')
    .controller('WarehouseDetailController', function ($scope, $rootScope, $stateParams, entity, Warehouse, Product) {
        $scope.warehouse = entity;
        $scope.load = function (id) {
            Warehouse.get({id: id}, function(result) {
                $scope.warehouse = result;
            });
        };
        var unsubscribe = $rootScope.$on('modernApp:warehouseUpdate', function(event, result) {
            $scope.warehouse = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
