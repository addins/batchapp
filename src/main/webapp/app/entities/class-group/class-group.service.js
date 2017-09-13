(function() {
    'use strict';
    angular
        .module('batchappApp')
        .factory('ClassGroup', ClassGroup);

    ClassGroup.$inject = ['$resource'];

    function ClassGroup ($resource) {
        var resourceUrl =  'api/class-groups/:id';

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
