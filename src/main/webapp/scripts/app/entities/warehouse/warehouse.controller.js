'use strict';

angular.module('modernApp')
    .controller('WarehouseController', function ($scope, $state, $modal, Warehouse, ParseLinks) {
      
        $scope.warehouses = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Warehouse.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.warehouses = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.warehouse = {
                location: null,
                id: null
            };
        };
    });
