// gallery container
var $rgGallery			= $('#rg-gallery'),
// carousel container
$esCarousel			= $rgGallery.find('div.es-carousel-wrapper'),
// the carousel items
$items				= $esCarousel.find('ul > li'),
// total number of items
itemsCount			= $items.length;

Gallery				= (function() {
  //gallery function
})();

Gallery.init();

_initCarousel	= function() {
	$esCarousel.show().elastislide({
		imageW 	: 65,
		onClick	: function( $item ) {
			if( anim ) return false;
			anim	= true;
			// on click show image
			_showImage($item);
			// change current
			current	= $item.index();
		}
	});

	// set elastislide's current to current
	$esCarousel.elastislide( 'setCurrent', current );

},
