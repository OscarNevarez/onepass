(function() {
    'use strict';

    angular
        .module('onepassApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('website', {
            parent: 'entity',
            url: '/website',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Websites'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/website/websites.html',
                    controller: 'WebsiteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('website-detail', {
            parent: 'entity',
            url: '/website/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Website'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/website/website-detail.html',
                    controller: 'WebsiteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Website', function($stateParams, Website) {
                    return Website.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'website',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('website-detail.edit', {
            parent: 'website-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/website/website-dialog.html',
                    controller: 'WebsiteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Website', function(Website) {
                            return Website.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('website.new', {
            parent: 'website',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/website/website-dialog.html',
                    controller: 'WebsiteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                domain: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('website', null, { reload: 'website' });
                }, function() {
                    $state.go('website');
                });
            }]
        })
        .state('website.edit', {
            parent: 'website',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/website/website-dialog.html',
                    controller: 'WebsiteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Website', function(Website) {
                            return Website.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('website', null, { reload: 'website' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('website.delete', {
            parent: 'website',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/website/website-delete-dialog.html',
                    controller: 'WebsiteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Website', function(Website) {
                            return Website.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('website', null, { reload: 'website' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
