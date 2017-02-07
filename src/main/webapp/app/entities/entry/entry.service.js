(function() {
    'use strict';
    angular
        .module('onepassApp')
        .factory('Entry', Entry);

    Entry.$inject = ['$resource'];

    function Entry ($resource) {
        var resourceUrl =  'api/entries/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
