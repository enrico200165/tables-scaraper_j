var SiteUi = {

	//properties
    ajax_controller_url: '/ajax/controller.php',
    datepickers: {},

	//methods
	init: function() {
        // Bind events to site_search
        this.site_search_events('.site_search');

        // Hide D Ad (or not) if site sidebar has greater height than main content container
        this.remove_if_sidebar_is_taller_than_main('#ad_D');

        // // Load sticky sidebar ad plugin
        if( $('html').hasClass('no-touch') ) {
            $('.pinned_container').stick_in_parent({
                parent: '#site_content'
            });
        }

        // Bind cancel_x for WLJT Banner
        this.bind_wljt_banner_cancel_x('.wljt_banner .cancel_x');

        // Resize Home Page Seasonal Teaser headline/recent stories carousel
        this.resize_seasonal_teaser('.copy--seasonal_report_teaser-headline', '.seasonal_reports_teaser__item--upcoming_reports');

        // Load all instances of Tooltipster plugin
        this.load_tooltipster_tooltips({
            // General tooltips
            ".has_tooltip": { /*theme: '.tooltip_style',*/ interactive: true, hideOnClick: true },
            ".dest_dotRating": { interactive: true, offsetY: -6, functionInit: this._tooltipster_load_dotRating_tooltip() }
        });

        // Initilaize pickers
        this.pickers_init('.picker');

        // Initialize and bind change events for Datepicker plugin on Hotel Finder widgets
        this.datepickers_init( ['hotelFinder_pageWidget_checkin'] );

        // Initialize widgets
        Widget_BookYourTrip.init();
        Widget_BeenThere_Wishlist_Btns.init();
        Widget_SpotList.init();

        // Multi-line ellipses
        $('.js-is-truncated').succinct({ size: 116 });

        // Bind breadcrumb click toggle
        this.bind_breadcrumbDropdownToggle('.breadcrumbs .breadcrumb a');

        // Initialize sidebar accordion (TO DO: allow for multiple accordions)
        this.accordion_init('.accordion');

        // Bind feedback click toggle
        this.bind_feedbackToggle('.fdb_link');

        // Initialize StickyNavbar plugin (attraction pages)
        /*
            *** TEMPORARILY DISABLED TILL LESS BUGGY JS CAN BE FOUND (TRY PIN.JS plus a spyscroll plugin) ***

        $('.attraction_pageNav').stickyNavbar({
            selector: 'li',
            animateCSS: false
        });
        */

        // Load polyfills
        if( !Modernizr.csscolumns ) {
            $.getScript('/js/polyfills/jquery.columnizer.js', function() {
                $('.exploreBox_spots_category, .destinations_topPage_top10_content ol').columnize({columns: 2});
            });
        }

	},

    site_search_events: function( site_search_selector ) {
        //While text input focus, keep search box "active"
        $(site_search_selector)
            .find('.site_search__input_text')
                .on('focus', function() {
                    $(this).closest(site_search_selector).addClass('is-active');
                })
                .on('blur', function() {
                    $(this).closest(site_search_selector).removeClass('is-active');
                });
    },

    remove_if_sidebar_is_taller_than_main: function( selector ) {
        if( $('.site_sidebar').height() > $('main').height() ) {
            $(selector).remove();
        }
    },

    bind_wljt_banner_cancel_x: function( cancel_x_selector ) {
        $(cancel_x_selector).click( function() {

            $(this)
                // 1. Immediately hide banner
                .parent()
                    .hide()
                // 2. Remove margin-adjustment class on homepage overlay site_header
                .siblings('.site_header_new')
                    .removeClass('site_header_new--below_wljt_banner');

            // 2. Set cookie to not show banner after clicking
            $.post( SiteUi.ajax_controller_url, {
                'ajax_request': 'set_cookie',
                'cookie': 'wljt_banner_ad'
            });
        });
    },

    resize_seasonal_teaser: function( headline_selector, story_carousel_selector ) {
        var headline_text = $(headline_selector).html();
        
        if( headline_text && headline_text.length > 24 ) {
            $(story_carousel_selector).addClass(story_carousel_selector.substring(1) + '--condensed');
        }
    },
    
	pickers_init: function( picker_selector ) {
		$(picker_selector).each( function(i, picker) {
            //
            // *** TO DO: make Picker a separate object with states, methods for setting/getting, updating ***
            //
            //      * Add keyboard interaction *
            //      * revert to native select on mobile devices *

            //If picker_value isn't set already (via PHP or otherwise), set the current selected item to that of the first list item
            if( $(picker).find('.picker_value').is(':empty') ) {
                var first_item = $(picker).find('.picker_list li:first-child');
                var first_item_val = first_item.data('value');
                var first_item_html = first_item.html();

                $(picker)
                    .find('.picker_value')
                        .data('current-value',first_item_val)
                        .attr('data-current-value',first_item_val)
                        .html( first_item_html )
                        .end()
                    // For pickers that also need javascript-independent forms
                    .find('input[type="hidden"]')
                        .val( first_item_val );

                first_item.addClass('selected');
            }
            //Otherwise, select the corresponding picker_list item
            else {
                var current_val = $(picker).find('.picker_value').data('current-value');
                
                $(picker).find('.picker_list [data-value="'+current_val+'"]').addClass('selected');
            }

            $(picker).not('.disabled')
                //On clicking (non-disabled) input field...
                .find('.picker_inputField')
                    .click( function() {
                        var this_picker = $(this).parents(picker_selector);
                        
                        //toggle "active" class
                        this_picker.toggleClass('active');
                        
                        //close menu on clicking outside of the picker
                        $(document).one('click', { el: this_picker }, function close_picker_menu (event) {

                            var picker_el = event.data.el;

                            if (!$(event.target).closest(picker_el).length) {
                                if( picker_el.hasClass('active') ) {
                                    picker_el.removeClass('active');
                                }
                            }
                            else {
                                $(document).one('click', { el: picker_el }, close_picker_menu);
                            }
                        });
                        
                    })
                    .end()

                //On clicking a (non-disabled) item...
                .find('li:not(.dropdown_cat_label)')
                    .click( function() {
                        var val = $(this).data('value');
                        var html = $(this).html();

                        var picker = $(this).parents(picker_selector);
                        var inputField = picker.find('.picker_value');
                        var original_val = inputField.data('current-value');

                        var callback = picker.data('select-js-callback');

                        // If picker has a callback "registered" (i.e. a 'data-select-js-callback' attr is defined)...
                        if( callback ) {
                            // 1. Toggle "selected" class on item, remove from other items
                            $(this)
                                .addClass('selected')
                                .siblings()
                                    .removeClass('selected');

                            // 2. Temporarily set inputField's data attr to the selection
                            inputField
                                .data('current-value',val)
                                .attr('data-current-value',val);

                            // 3. Call method (send picker object and selected value as args)
                            var callback_objs = callback.split('.');
                            var obj = callback_objs[0];
                            var method = callback_objs[1];

                            var jqxhr = window[obj][method]( picker, val );

                            jqxhr
                                .fail( function() {
                                    //reset original vals
                                    inputField
                                        .data('current-value',original_val)
                                        .attr('data-current-value',original_val);
                                })
                                .done( function() {
                                    //set picker's inputField label to the selection
                                    inputField
                                        .data('current-value',val)
                                        .attr('data-current-value',val)
                                        .html( html );

                                    //set hidden input field value (if it exists)
                                    picker.find('input[type="hidden"]').val( val );
                                })
                                .always( function() {
                                    //toggle "active" class on picker
                                    picker.toggleClass('active');
                                });

                            // TODO: callback hooks should be assigned via an API for a picker "plugin":
                            //          ajax_on_select: true;   
                            //          ...??
                            //          itemSelected() -> if returns true, select item, deactivate picker;
                            //                            if returns false, don't select item, don't deactivate

                        }
                        else {
                            $(this)
                                //toggle "selected" class on item, remove from other items
                                .addClass('selected')
                                .siblings()
                                    .removeClass('selected')
                                    .end()

                                .parents(picker_selector)
                                    //toggle "active" class on picker
                                    .toggleClass('active')
                                    
                                    //set picker's inputField label to the selection
                                    .find('.picker_value')
                                        .data('current-value',val)
                                        .attr('data-current-value',val)
                                        .html( html )
                                        .end()
                                    //set hidden input field value (if it exists)
                                    .find('input[type="hidden"]').val( val );
                        }
                    });
        });
        
	},

    accordion_init: function( accordion_selector ) {
        $(accordion_selector).find('.has-toggle').click(function(el){
            // Prevent links to pages (<a>s that are children) from toggling accordion panels
            if( el.target !== this ) { return; }

            // Expand or collapse panels on click
            $(this)
                .toggleClass('is-expanded')                     // add 'is-expanded' to list item
                .next('ul')                                     // target actual nested list (which are adjacent siblings)
                    .slideToggle('fast', function() {
                        // if( $(this).hasOverflow() ) {
                        //     $(this).addClass('has-overflow');
                        // }
                        $(this).toggleClass('is-expanded');               // add 'is-expanded' to just-expanded list element
                        // Recalculates positioning of sticky elements as element heights change (stick_kit plugin)
                        $(document.body).trigger("sticky_kit:recalc");
                    });

                    //.end()
                //.siblings('ul.is-expanded').not( $(this).next() ).slideUp('fast').toggleClass('is-expanded');
        });

        // Disables "empty" links that don't have an href value set (might be better to prevent <a> tags via PHP)
        $(accordion_selector).find('a[href=""]')
            .click(function(event) { event.preventDefault(); })
            .css('cursor', 'default')
            .hover(function() {
                $(this).css("color","#ffffff");
            });

        // $(accordion_selector).find('ul.is-expanded')
        //     .on('scroll', function() {
        //         if($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight) {
        //             // hide an overlayed arrow or something that shows there's more below
        //         }
        //     });
        //
        // *** ALSO add a toggle to show arrow on expand if the bottom isn't already visible ***
    },
	
	bind_feedbackToggle: function( link_selector ) {
		$(link_selector).click( function() {
            $('.fdb_hook').toggleClass('active');
        });
	},

    bind_breadcrumbDropdownToggle: function( link_selector ) {
        $(link_selector).click( function() {
            $(this).parent().toggleClass('active');
        });
    },

    datepickers_init: function( datepicker_ids ) {

        var self = this;

        $.each( datepicker_ids, function( i, datepicker_id ) {

            self.datepickers[datepicker_id] = new Pikaday({ field: document.getElementById(datepicker_id) });

            $('#'+datepicker_id).change( function() {

                var selected_date = self.datepickers[datepicker_id].getDate();
                
                $(this).siblings('[name="selDay"]').val(selected_date.getDate());
                $(this).siblings('[name="selMonth"]').val(selected_date.getMonth()+1);
                $(this).siblings('[name="selYear"]').val(selected_date.getFullYear());

                $(this).siblings('[name="F1Day"]').val(selected_date.getDate());
                $(this).siblings('[name="F1Month"]').val(selected_date.getMonth()+1);

                $(this).siblings('[name="busDay"]').val(selected_date.getDate());
                $(this).siblings('[name="busMonth"]').val(selected_date.getMonth()+1);
                $(this).siblings('[name="busYear"]').val(selected_date.getFullYear());
            });

        });

    },

    redirect_to_user_login: function( params ) {
        window.location = 'https://www.japan-guide.com/login?' + $.param(params);
    },

    ajaxError: function( jqXhr, error_status, error_code ) {

        var message;

        if( error_status === 'timeout' ) {
            message = "Sorry, your request timed out. Please try again.";
        }
        else {
            switch(error_code) {
                case "nocontent":
                    message = "Sorry, we couldn't save your request. Please try again later!";
                    break;
                default:
                    message = 'Sorry, an error occurred (' + error_code + ').';
            }
        }

        // Simply alert the error message
        alert(message);
    },

    load_tooltipster_tooltips: function ( tooltipsToLoad ) {
        for ( var selector in tooltipsToLoad ) {
            
            var settingsToLoad = tooltipsToLoad[selector];

            if( 'separateContent' in settingsToLoad ) {
                // If 'separateContent' class is specified, add new 'functionBefore' to Tooltipster config
                settingsToLoad.settings.functionBefore = this._tooltipster_set_separateContent( settingsToLoad.separateContent );

                // Override 'settingsToLoad' with config including additional 'functionBefore'
                settingsToLoad = settingsToLoad.settings;
            }

            // Load Tooltipster plugin with 'settingsToLoad'
            $( selector ).tooltipster( settingsToLoad );
        }
    },

    _tooltipster_set_separateContent: function ( separateContent_selector ) {
        return function( origin, continueTooltip ) {
            var classes = origin.attr("class").split(" ");
            var contentClass = classes[classes.length-1];
            
            var tooltipContent = $( separateContent_selector + '.' + contentClass ).html();

            origin.tooltipster('content', tooltipContent);
            continueTooltip();
        };
    },

    _tooltipster_load_dotRating_tooltip: function() {
        return function (origin) {
            //var dots_el = origin.find('.dest_dotRating');
            var label = '<span class="dots_label">' + origin.data('dots-tooltiplabel') + '</span>';
            var expl  = '<span class="dots_expl">' + origin.data('dots-tooltipexpl') + '</span>';
            
            var tooltipContent = $('<div class="dotRating_tooltip">'+label+expl+'</div>');

            return tooltipContent;
        };
    }
};

var Widget_BookYourTrip = {

    //properties
    widget_name: 'booking',

    active_booking_type: '',
    active_booking_type_default_place_id: '',
    active_booking_type_default_place_name: '',

    bookingTypeMenu_selector: '.bookYourTrip_bookingTypeMenu',
    widgetContent_selector: '.bookYourTrip_content',
    place_list_selector: '.place_list',
    

    //methods
    init: function() {
        //Set initial active booking type from data attribute on widgetContent_selector (set via PHP)
        this.active_booking_type = $(this.widgetContent_selector).data('active-booking-type');

        //Set initial active booking type's default place data
        this.set_active_booking_type_default_place_data();

        //Bind menu toggle
        this.bind_menuToggle( this.bookingTypeMenu_selector );

        //Bind initial datepickers
        this.bind_pickers( this.active_booking_type, true );
    },

    bind_menuToggle: function( buttonGroup_selector ) {

        var self = this;

        $(buttonGroup_selector).find('li').click( function() {
            var btn_booking_type = $(this).data('booking-type');
            var default_place_id = $(this).data('default-id');

            if( btn_booking_type !== self.active_booking_type ) {
                //Toggle the "active" class on button group buttons
                $(this)
                    .addClass('active')
                    .siblings().removeClass('active');

                //Change the active current active booking type
                self.active_booking_type = btn_booking_type;

                //Load the booking type view (only if it's not already the active one)
                self.load_booking_type_view( btn_booking_type, default_place_id );
            }
        });
    },

    bind_pickers: function( booking_type, datepickers_only ) {

        var datepicker_ids;
        
        switch(booking_type) {
            case "hotels":
                datepicker_ids = ['bookYourTrip_hotels_checkin'];
                break;
            case "flights":
                datepicker_ids = ['bookYourTrip_flights_departure_date'];
                break;
            case "bus":
                datepicker_ids = ['bookYourTrip_bus_departure_date'];
                break;
        }
        SiteUi.datepickers_init( datepicker_ids );

        if( !datepickers_only ) {
            SiteUi.pickers_init( this.widgetContent_selector+' .picker' );
        }
        
    },

    get_active_booking_type_default_place_id: function( buttonGroup_selector ) {
        return $(buttonGroup_selector).find('[data-booking-type="'+this.active_booking_type+'"]').data('default-id');
    },

    get_active_booking_type_default_place_name: function() {
        return $(this.widgetContent_selector).find( this.place_list_selector + ' [data-value="'+this.active_booking_type_default_place_id+'"]').eq(0).html();
    },

    set_active_booking_type_default_place_data: function() {
        var default_place_bound_data = $(this.bookingTypeMenu_selector).find('[data-booking-type="'+this.active_booking_type+'"]');

        //Set Widget_BookYourTrip properties
        this.active_booking_type_default_place_id = this.get_active_booking_type_default_place_id( this.bookingTypeMenu_selector );
        this.active_booking_type_default_place_name = this.get_active_booking_type_default_place_name();

        //If it's not already set, bind the name to the data holder (the booking type button in the button group menu)
        if( !default_place_bound_data.data('default-name') ) {
            default_place_bound_data.data('default-name', this.active_booking_type_default_place_name );
        }
    },

    prepend_new_default_place: function( booking_type ) {
        // Assuming the default name wasn't found in the place_list,
        // this gets the default name from the data holder for the booking type (the booking type button in the button group menu)
        var default_place_bound_name = $(this.bookingTypeMenu_selector).find('[data-booking-type="'+booking_type+'"]').data('default-name');
        var default_place = '<li data-value="' + this.active_booking_type_default_place_id + '">' + default_place_bound_name + '</li>';
        
        $(this.place_list_selector).prepend( default_place );
    },

    load_booking_type_view: function( booking_type, default_place_id ) {

        $.ajax({
            url:  SiteUi.ajax_controller_url,
            type: 'get',
            data: {
                'ajax_request': 'call_widget_method',
                'widget_name': this.widget_name,
                'widget_method': 'load_view',
                'widget_method_args': {
                    'view': booking_type + '_finder'
                },
                'widget_data': {
                    'active_booking_type': booking_type,
                    'default_place_id': default_place_id
                }
            },
            context: this,
            beforeSend: function() {
                // TODO: Spinner
            },
            success: function(response_html) {
                //Replace widget content container's HTML with response HTML
                $(this.widgetContent_selector).html(response_html);

                //Set new default place data
                this.set_active_booking_type_default_place_data();

                //Check if default place is in the active place list (via get_active_booking_type_default_place_name() ).
                //If not, generate new one at the top of the list
                if( !this.get_active_booking_type_default_place_name() ) {
                    this.prepend_new_default_place( booking_type );
                }

                //Re-bind pickers on load
                this.bind_pickers(booking_type);
            },
            error: function() {
                
            }
        });
    }

};

var Widget_BeenThere_Wishlist_Btns = {

    //properties
    widget_name: 'beenThere_wishlist_btns',

    widget_selector: '.beenThere_wishlist_btns',
    button_selector: '.travelExp_btn',

    place_rater_selector: '.place_rater',
    place_rater_star_class: '.star',
    place_rater_star_selector:  '',         // set via init


    //object properties
    buttons: {
        beenThere: {
            selector: '.icon_destinations_beenThere',
            el: null,
            list: 'beenThere',
            state: null,                     // 'inactive' | 'active' | 'disabled'
            messages: {
                hover_inactive: 'I\'ve been to this place',
                hover_active: 'You\'ve been to this place',
                add_success: 'You\'ve been to this place',
                delete_success: 'This place was removed from your travel experience list',
                already_added: '',
                already_deleted: ''
            }
        },
        wishlist: {
            selector: '.icon_destinations_wishlist',
            el: null,
            list: 'wishlist',
            state: null,                     // 'inactive' | 'active' | 'disabled'
            messages: {
                hover_inactive: 'Add this place to your wishlist',
                hover_active: 'This place is on your wishlist',
                add_success: 'This place was added to your wishlist',
                delete_success: 'This place was removed from your wishlist',
                already_added: '',
                already_deleted: ''
            }
        }
    },

    user: {
        id: null,                           // user id (int)
        is_loggedIn: null,                  // boolean
        lists_place_added_to: [],           // array of lists user has added place to ( ['beenThere','wishlist'] )

        has_added_place_to_list: function( list ) {
            return ( $.inArray( list, this.lists_place_added_to ) !== -1 ) ? true : false;
        },

        add_to_lists_place_added_to: function( list ) {
            this.lists_place_added_to.push(list);
        },

        remove_from_lists_place_added_to: function( list ) {
            var i = this.lists_place_added_to.indexOf( list );
            if( i !== -1) {
                this.lists_place_added_to.splice(i, 1);
            }
        }
    },

    place: {
        id: null,
        name: null,
        page_rating_data: {}
    },


    // methods
    init: function() {
        // 1. Set properties
        this.user.id = $(this.widget_selector).data('user-id');
        this.user.is_loggedIn = $(this.widget_selector).data('user-is-loggedin');
        this.place.id = $(this.widget_selector).data('place-id');
        this.place.name = $(this.widget_selector).data('place-name');
        this.place.page_rating_data = $(this.widget_selector).data('page-rating-data');
        this.place_rater_star_selector = this.place_rater_selector + ' ' + this.place_rater_star_class;

        // 2. Set initial place_rater CSS
        $(this.place_rater_selector).children('.place_rater_content').css({
            "margin-left": function() { return -( $(this).actual('outerWidth') ); }
        });

        // 3. Set initial button data
        for( var button in this.buttons ) {
            this.set_initial_button_data( button );
        }

        // 4. Set initial star rating (if rating set)
        this.activate_initial_star_rating();

        // 5. Load tooltips for widget
        SiteUi.load_tooltipster_tooltips( this.get_star_rating_tooltip_options() );

        // 6. Bind interaction events
        this.bind_foot_heart_clicks();
        this.bind_rating_star_clicks();
        this.bind_place_rater_hover();
        this.bind_rating_clearer_click();
    },

    set_initial_button_data: function( button ) {
        var btn_obj = this.buttons[button];

        // Set reference to button jQuery object
        btn_obj.el = $(this.widget_selector + ' ' + btn_obj.selector);

        // Replace "this place" string with actual place name in messages
        if( this.place.name ) {
            for( var message_type in btn_obj.messages ) {
                btn_obj.messages[message_type] = btn_obj.messages[message_type].replace(/this place/gi, this.place.name );
            }
        }

        // Set initial button state (as "active" class is set initially via PHP, this only updates the object state)
        this.change_button_state( btn_obj, this.get_initial_button_state( btn_obj ) );

        // Set initial lists user has added the place to
        if( btn_obj.state === 'active' ) {
            this.user.add_to_lists_place_added_to( btn_obj.list );
        }
    },

    get_initial_button_state: function( button ) {
        return ( button.el.data('user-has-added-to-list') ) ? 'active' : 'inactive';
    },

    activate_initial_star_rating: function() {
        var initial_rating = $(this.buttons.beenThere.el).data('rating');

        // If rating is 1-5, activate star rating (if 0 or empty, don't)
        if( initial_rating ) {
            this.set_star_rating( $(this.place_rater_star_selector+'[data-rate="'+initial_rating+'"]') );
        }
    },

    get_star_rating_tooltip_options: function() {
        var rater = $(this.place_rater_selector);
        var options = {
            // Star rating tooltips
            ".place_rater .star, .place_rater .cancel_x": {
                speed: 0,
                delay: 0,
                hideOnClick: true,
                offsetY: -8,
                functionBefore: function( origin, continueTooltip ) {
                    // Don't show tooltips while place rater is sliding or on disabled elements
                    if( !rater.hasClass('sliding') && !$(this).hasClass('disabled') ) {
                        continueTooltip();
                    }
                }
            }
        };
        return options;
    },

    bind_foot_heart_clicks: function() {
        var widget = this;

        $(widget.button_selector).click( function() {

            var button = widget.buttons[ $(this).data('list') ];
            var ajax_method, js_method, has_added_boolean;
            
            switch( button.state ) {
                case "inactive":
                    ajax_method = 'add_place_to_list';
                    js_method = 'add_to_lists_place_added_to';
                    has_added_boolean = false;
                    break;

                case "active":
                    ajax_method = 'remove_place_from_list';
                    js_method = 'remove_from_lists_place_added_to';
                    has_added_boolean = true;
                    break;

                default:
                    //do nothing (in case button is disabled or stateless)
                    break;
            }

            //User must be logged in for buttons to work
            if( widget.user.is_loggedIn ) {
                //Check if user has/has not added place to JS obj list, add/remove from user object and DB accordingly
                if( widget.user.has_added_place_to_list( button.list ) === has_added_boolean ) {
                    widget.user[js_method]( button.list );
                    widget.update_list( button.list, ajax_method );
                }
            }
            //If user is not logged in, prompt them to log in (sending params to return to the page with button updated)
            else {
                var login_params = {
                    'aACTION': widget.widget_name,
                    'aOBJECT': widget.place.id,
                };
                SiteUi.redirect_to_user_login( login_params );
            }
        });
    },

    bind_rating_star_clicks: function() {
        var widget = this;
        
        $(widget.place_rater_star_selector).click( function() {
            if( !$(this).hasClass('disabled') ) {
                widget.rate_place( $(this).data('rate'), $(this) );
            }
        });
    },

    bind_place_rater_hover: function() {
        var widget = this;
        var button = widget.buttons.beenThere;
        var place_rater_trigger = $(widget.place_rater_selector).parent();

        place_rater_trigger.hover(
            //On mouseenter, if the Foot is active and the rater is NOT open, open the rater after a slight delay
            function() {
                if( button.state==="active" && !$(widget.place_rater_selector).hasClass('active') ) {
                    setTimeout( function() {
                        // If mouse is STILL over the trigger area after the delay, open the rater
                        if( place_rater_trigger.is(":hover") ) { widget.toggle_place_rater('show'); }
                    }, 450);
                }
            },
            //On mouseleave, as long as the Foot is active and the rater IS open, hide the rater
            function() {
                if( button.state==="active" && $(widget.place_rater_selector).hasClass('active') ) {
                    setTimeout( function() {
                        // If mouse is STILL OUTSIDE of the trigger area after the delay, hide the rater
                        if( !place_rater_trigger.is(":hover") ) { widget.toggle_place_rater('hide'); }
                    }, 450);
                }
            }
        );
    },

    bind_rating_clearer_click: function() {
        var widget = this;

        $(widget.place_rater_selector + ' .cancel_x').click( function() {
            // To clear rating, call rate_place() with a rating of 0 (btn element param not needed)
            widget.rate_place( 0 );
        });
    },

    change_button_state: function( button, new_state ) {
        // change button state property
        this.buttons[button.list].state = new_state;

        // add/remove appropriate class
        switch( new_state ) {
            case "active":
                $(button.el)
                    .removeClass('disabled')
                    .addClass('active');
                // change tooltip message
                this.set_tooltip_message( button, button.messages['hover_' + new_state] );
                break;

            case "inactive":
                $(button.el)
                    .removeClass('disabled')
                    .removeClass('active');
                // change tooltip message
                this.set_tooltip_message( button, button.messages['hover_' + new_state] );
                break;

            case "disabled":
                $(button.el)
                    .removeClass('active')
                    .addClass('disabled');
                break;
        }
    },

    update_list: function( list, update_type ) {
        var button = this.buttons[list];
        var current_btn_state = button.state;
        var new_btn_state = ( update_type === "add_place_to_list" ) ? "active" : "inactive";

        $.ajax({
            url:  SiteUi.ajax_controller_url,
            type: 'post',
            data: {
                'ajax_request': 'call_widget_method',
                'widget_name': this.widget_name,
                'widget_method': update_type,
                'widget_method_class': 'user',
                'widget_method_args': {
                    'place_id': this.place.id,
                    'list': list,
                    'place_rating_data': this.place.page_rating_data
                }
            },
            context: this,
            beforeSend: function() {
                // Disable button
                this.change_button_state( button, 'disabled' );
            },
            success: function( response ) {
                if(response==='success') {
                    // For the "been there" list...
                    if( list==='beenThere' ) {
                        switch( update_type ) {
                            // If adding a place, prompt user to rate the place (show place rater)
                            case 'add_place_to_list':
                                this.toggle_place_rater('show');
                                break;
                            //If removing a place, clear stars and rating and hide place rater
                            case 'remove_place_from_list':
                                this.clear_star_rating();
                                this.update_rating_badge(0);
                                this.toggle_place_rater('hide');
                                break;
                        }
                    }
                }
            },
            error: function(xhr, status, errorThrown) {
                // If server-side authentication fails, redirect to login
                if(xhr.status == 401) {
                    var login_params = {
                        'aACTION': this.widget_name,
                        'aOBJECT': this.place.id
                    };
                    SiteUi.redirect_to_user_login( login_params );
                }
                // Otherwise, handle ajax error
                else {
                    SiteUi.ajaxError(xhr, status, errorThrown);
                }
            },
            complete: function(xhr, status) {
                // Change button state (to new state if successful, back to original state if unsuccessful)
                var btn_state = (status === 'success') ? new_btn_state : current_btn_state;
                this.change_button_state( button, btn_state );
            }
        });
    },

    set_tooltip_message: function( button, message ) {
        $(button.el).tooltipster('content',message);
    },

    toggle_place_rater: function( action ) {
        var rater = $(this.place_rater_selector);
        var rater_content = rater.children('.place_rater_content');
        
        if( !$(rater).hasClass('sliding') ) {
            var animate_props = {
                marginLeft: parseInt(rater_content.css('margin-left')) === 0 ? -rater_content.outerWidth() : 0
            };
            var animate_options = {
                start: function() { rater.addClass('sliding'); },
                done:  function() { rater.removeClass('sliding'); }
            };

            // 2. Toggle "active" class and rater before or after animation is finished
            switch( action ) {
                case "show":
                    rater_content.animate( animate_props, animate_options );
                    rater.addClass('active');
                    break;
                case "hide":
                    rater.removeClass('active');
                    rater_content.animate( animate_props, animate_options );
                    break;
            }
        }
    },

    set_star_rating: function( active_rate_star_button ) {
        active_rate_star_button
            .addClass('active')
            .nextAll(this.place_rater_star_class).addClass('active').end()
            .prevAll(this.place_rater_star_class).removeClass('active');
    },

    clear_star_rating: function() {
        $(this.place_rater_star_selector).removeClass('active');
    },

    update_rating_badge: function( rating ) {
        // Updates current rating data value and data-rating attribute to show rating badge over foot icon via CSS
        $(this.buttons.beenThere.el)
            .data('rating',rating)
            .attr('data-rating',rating);
    },

    rate_place: function( rating, star_button_clicked ) {
        $.ajax({
            url:  SiteUi.ajax_controller_url,
            type: 'post',
            data: {
                'ajax_request': 'call_widget_method',
                'widget_name': this.widget_name,
                'widget_method': 'rate_place',
                'widget_method_class': 'user',
                'widget_method_args': {
                    'place_id': this.place.id,
                    'rating': rating,
                    'place_rating_id': this.place.page_rating_data.id
                }
            },
            context: this,
            beforeSend: function() {
                // Disable star buttons
                $(this.place_rater_star_selector).addClass('disabled');
            },
            success: function( response ) {
                if(response==='success') {
                    var clear_rating = (parseInt(rating) === 0) ? true : false;

                    // Add/remove "active" class to/from appropriate stars
                    clear_rating ? this.clear_star_rating() : this.set_star_rating( star_button_clicked );

                    // After a brief delay...
                    var widget = this;
                    setTimeout( function() {
                        // If the rating is 1-5 (i.e. not clearing the rating), close place rater
                        if( !clear_rating ) { widget.toggle_place_rater('hide'); }

                        // Update rating data value and badge
                        widget.update_rating_badge(rating);
                    }, 450);
                }
            },
            error: function(xhr, status, errorThrown) {
                // If server-side authentication fails, redirect to login
                if(xhr.status == 401) {
                    var login_params = {
                        'aACTION': this.widget_name,
                        'aOBJECT': this.place.id
                    };
                    SiteUi.redirect_to_user_login( login_params );
                }
                // Otherwise, handle ajax error
                else {
                    SiteUi.ajaxError(xhr, status, errorThrown);
                }
            },
            complete: function() {
                // Re-enable star buttons (regardless of server response)
                $(this.place_rater_star_selector).removeClass('disabled');
            }
        });
    }

};

var Widget_SpotList = {
    //properties
    widget_name: 'spotList',

    spotList_id: '',
    data_class:  '',

    view_control_picker_selector: '.spotList_view_control',

    //methods
    init: function() {
        // Set widget properties
        this.spotList_id = $(this.view_control_picker_selector).parent().data('spotlist-id');
        this.data_class = $(this.view_control_picker_selector).parent().data('data-class');

        // Add data attribute to pickers to trigger ajax calls from this widget on clicking an option
        this.inject_picker_callback_triggers('Widget_SpotList.change_view');
    },

    inject_picker_callback_triggers: function( callback_name ) {
        $( this.view_control_picker_selector )
            .data('select-js-callback', callback_name)
            .attr('data-select-js-callback', callback_name);
    },

    change_view: function( picker_obj ) {
        var current_group_by_id = $( this.view_control_picker_selector + '.group_by_options' ).find('.picker_value').data('current-value');
        var current_order_by_id = $( this.view_control_picker_selector + '.order_by_options' ).find('.picker_value').data('current-value');
        
        return $.ajax({
            url:  SiteUi.ajax_controller_url,
            type: 'post',
            data: {
                'ajax_request': 'call_widget_method',
                'widget_name': this.widget_name,
                'widget_data': {
                    'data_class': this.data_class,
                    'spotList': {
                        'id': this.spotList_id,
                        'group_by_default_id': current_group_by_id,
                        'order_by_default_id': current_order_by_id
                    }
                },
                'widget_method': 'load_view',
                'widget_method_args': {
                    'view': 'list'
                }
            },
            beforeSend: function() {
                // Disable the picker and show ajax loader
                $(picker_obj)
                    .addClass('disabled')
                    .find('.selected')
                        .append('<div class="ajaxLoader_dots"></div>');
            },
            success: function( response_html ) {
                // Replace list HTML with response HTML
                $('.spotList_list')
                    .css('opacity', 0)
                    .html( response_html )
                    .slideDown('slow')
                    .animate(
                        { opacity: 1 },
                        { queue: false, duration: 'slow' }
                    );

                // Re-bind all necessary listeners (dot rating tooltips)
                SiteUi.load_tooltipster_tooltips({
                    ".spotList_list .dest_dotRating": {
                        interactive: true,
                        offsetY: -6,
                        functionInit: SiteUi._tooltipster_load_dotRating_tooltip()
                    }
                });
            },
            error: function(xhr, status, errorThrown) {
                SiteUi.ajaxError(xhr, status, errorThrown);
            },
            complete: function() {
                // Re-enable the picker and hide ajax loader
                $(picker_obj)
                    .removeClass('disabled')
                    .find('.ajaxLoader_dots')
                        .remove();
            }
        });
    }
};

SiteUi.init();