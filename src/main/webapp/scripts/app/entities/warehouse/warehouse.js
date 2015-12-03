'use strict';

angular.module('modernApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('warehouse', {
                parent: 'entity',
                url: '/warehouses',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'modernApp.warehouse.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/warehouse/warehouses.html',
                        controller: 'WarehouseController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('warehouse');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('warehouse.detail', {
                parent: 'entity',
                url: '/warehouse/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'modernApp.warehouse.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/warehouse/warehouse-detail.html',
                        controller: 'WarehouseDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('warehouse');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Warehouse', function($stateParams, Warehouse) {
                        return Warehouse.get({id : $stateParams.id});
                    }]
                }
            })
            .state('warehouse.new', {
                parent: 'warehouse',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/warehouse/warehouse-dialog.html',
                        controller: 'WarehouseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    location: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('warehouse', null, { reload: true });
                    }, function() {
                        $state.go('warehouse');
                    })
                }]
            })
            .state('warehouse.edit', {
                parent: 'warehouse',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/warehouse/warehouse-dialog.html',
                        controller: 'WarehouseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Warehouse', function(Warehouse) {
                                return Warehouse.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('warehouse', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('warehouse.delete', {
                parent: 'warehouse',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/warehouse/warehouse-delete-dialog.html',
                        controller: 'WarehouseDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Warehouse', function(Warehouse) {
                                return Warehouse.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('warehouse', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
