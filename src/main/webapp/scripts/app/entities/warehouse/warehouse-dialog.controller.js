'use strict';

angular.module('modernApp').controller('WarehouseDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Warehouse', 'Product',
        function($scope, $stateParams, $modalInstance, entity, Warehouse, Product) {

        $scope.warehouse = entity;
        $scope.products = Product.query();
        $scope.load = function(id) {
            Warehouse.get({id : id}, function(result) {
                $scope.warehouse = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('modernApp:warehouseUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.warehouse.id != null) {
                Warehouse.update($scope.warehouse, onSaveSuccess, onSaveError);
            } else {
                Warehouse.save($scope.warehouse, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
