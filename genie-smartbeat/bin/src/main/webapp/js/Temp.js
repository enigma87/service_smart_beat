fb.views.UserID = Backbone.View.extend({

    initialize: function () {
        this.template = _.template(fb.templateLoader.get('UserID'));
        this.render();
    },

    render: function () {
        this.$el.html(this.template());
        return this;
    },

    events: {
        "click .post": "postMessage"
    },

    postMessage: function () {
        FB.login(function (response) {
            if (response.authResponse) {
                FB.api('/me', function (response) {
                    alert('Good to see you, ' + response.name + '.' + response.email);
                });
            } else {
                alert('User cancelled login or did not fully authorize.');
            }
        }, { scope: 'email' });

        FB.getLoginStatus(function (response) {
            if (response.status === 'connected') {
                var uid = response.authResponse.userID;
                var accessToken = response.authResponse.accessToken;
                alert('User ID :' + uid + "Access Token:" + accessToken);

            } else if (response.status === 'not_authorized') {
                // the user is logged in to Facebook, 
                // but has not authenticated your app
            } else {
                // the user isn't logged in to Facebook.
            }
        });

        return false;
    }
});
