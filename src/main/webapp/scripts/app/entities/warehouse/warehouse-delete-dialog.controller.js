'use strict';

angular.module('modernApp')
	.controller('WarehouseDeleteController', function($scope, $modalInstance, entity, Warehouse) {

        $scope.warehouse = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Warehouse.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });