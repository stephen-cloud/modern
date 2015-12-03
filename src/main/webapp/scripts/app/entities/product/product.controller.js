'use strict';

angular.module('modernApp')
    .controller('ProductController', function ($scope, $state, $modal, Product, ParseLinks) {
      
        $scope.products = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Product.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.products = result;
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
