'use strict';

angular.module('modernApp')
    .controller('ProductLowStockController', function ($scope, $state, $modal, Product, ParseLinks) {
      
        $scope.products = [];
        $scope.page = 0;
        $scope.limit = 10;
        $scope.loadLowStock = function() {
            Product.query({page: $scope.page, size: 20, limit: $scope.limit}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.products = result;
            });
        };
        $scope.loadPage = function(page, limit) {
            $scope.page = page;
            $scope.limit = limit;
            $scope.loadLowStock();
        };
        $scope.loadLowStock();


        $scope.refresh = function () {
            $scope.loadLowStock();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.product = {
                description: null,
                price: null,
                sku: null,
                stock: null,
                department: null,
                id: null
            };
        };
    });
