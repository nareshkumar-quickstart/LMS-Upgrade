var ui = function () {

    return {
		
		init:function()
		{
			if($("html").hasClass("no-touch"))
			{
				//	Desktop Specific
				//ui.mouseAnalyzer(0);
				$('.left-menu .side-menu-nav.items').slimscroll({
					height: '100%',
					color: '#fff',
					wheelStep: 5,
					alwaysVisible: true
				});
			}
			else
			{
				//	Mobile Specific
				$('.left-menu .side-menu-nav.items').slimscroll({
					height: '100%',
					color: '#fff',
					alwaysVisible: true
				});
				/*$(".wrapper-body").on("touchmove", function(event) {
					event.preventDefault();
					event.stopPropagation();
				});*/
			}
			
			ui.guide.init();
			ui.svgModal.init();
			ui.slide.init();
			ui.social.fb.init();
			
			//ui.warning.init();
			
			/*$("#right-side").click(function(e) {
				e.preventDefault();
				$("#wrapper").toggleClass("toggled-right");
				$("#wrapper").removeClass("toggled-left outline glossary");
			});
			$("#bottom-toggle").click(function(e) {
				e.preventDefault();
				$("#wrapper").toggleClass("toggled-bottom");
			});
			$("#top-toggle").click(function(e) {
				e.preventDefault();
				$("#wrapper").toggleClass("toggled-top");
			});*/
			$("#content-overlay").click(function(e) {
				e.preventDefault();
				if($("#wrapper").hasClass("toggled-left"))
				{
					ui.nav.options();
				}
			});
			$('.configures').on('click', '.dropdown-menu', function(){
				$(this).closest('.dropup').addClass('dontClose');
			});
			$('.configures > .dropup').on('hide.bs.dropdown', function(e) {
				if ( $(this).hasClass('dontClose') ){
					e.preventDefault();
				}
				$(this).removeClass('dontClose');
			});
		},
		
		dropBox:function(id,path,title)
		{
			var options = {
				files: [
					// You can specify up to 100 files.
					{'url': path, 'filename': title}
				],

				// Success is called once all files have been successfully added to the user's
				// Dropbox, although they may not have synced to the user's devices yet.
				success: function () {
					// Indicate to the user that the files have been saved.
					console.log("Success! Files saved to your Dropbox.");
				},

				// Progress is called periodically to update the application on the progress
				// of the user's downloads. The value passed to this callback is a float
				// between 0 and 1. The progress callback is guaranteed to be called at least
				// once with the value 1.
				progress: function (progress) {
					console.log("progress: "+progress);
				},

				// Cancel is called if the user presses the Cancel button or closes the Saver.
				cancel: function () {
					console.log("cancel");
				},

				// Error is called in the event of an unexpected response from the server
				// hosting the files, such as not being able to find a file. This callback is
				// also called if there is an error on Dropbox or if the user is over quota.
				error: function (errorMessage) {
					console.log("errorMessage: "+errorMessage);
				}
			};
			var button = Dropbox.createSaveButton(options);
			$("#"+id).parent().append(button);
			$("#"+id).remove();
		},
		
		video:
		{
			instance:'',
			player:function(path,id,_w,_h,_auto)
			{
				if(typeof _auto == "undefined")
				{
					_auto = false;
				}
				if(typeof _h == "undefined")
				{
					_h = 260;
				}
				if(typeof _w == "undefined")
				{
					_w = "100%";
				}
				
				if(typeof id != "undefined")
				{
					jwplayer.key="NWa+NruTBASm39QxfCBvuv1UblvSsMtD+mrZiJgnxNI=";
					ui.video.instance = jwplayer(id);
				}
				
				ui.video.instance.setup({
					file: path,//assets/uploads/fields.mp4',
					/*aspectratio: "16:9",*/
					height: _h,
					repeat: false,
					skin: {"name": "bekle"},
					stagevideo: false,
					stretching: "uniform",
					width: _w,
					autostart: _auto
				});
			}
		},
		
		pdf:function(file)
		{
			new PDFObject({ url: file }).embed("cd-modal-pdf");
		},
		
		nav:
		{
			cache:'',
			gIndex:0,
			gTerms:["WMI","PowerShell","Networking","CIM Analogy","Certification"],
			
			move:function(cond,pos)
			{
				switch (cond)
				{
					case "outline":
						$("#course-outline .side-menu-nav.items").slimScroll({scrollTo: pos});
					break;
				}
			},
			
			switchGlossary:function(trg,isForward)
			{
				if(isForward)
				{
					ui.nav.gIndex++;
				}
				else
				{
					ui.nav.gIndex--;
				}
				
				$('.cd-modal[data-modal="modal-dynamic"] > .cd-modal-content').html(' ');
				svg_modal_open($(trg));
			},
			
			nestedBtn:function()
			{
				$("#course-outline .hasChildren span").click(function(e) {
					$(this).parent().parent().toggleClass("expand");
				});
			},
			
			options:function ()
			{
				$("#wrapper").toggleClass("toggled-left");
			},
			
			home:function ()
			{
				if($("#wrapper").hasClass("glossary") || $("#wrapper").hasClass("material") || $("#wrapper").hasClass("labs") || $("#wrapper").hasClass("outline") || $("#wrapper").hasClass("mentoring"))
				{
					$("#wrapper").toggleClass("options");
				}
				else
				{
					$("#wrapper").toggleClass("toggled-left").toggleClass("options");
				}
				$("#wrapper").removeClass("toggled-right glossary material outline labs mentoring");
			},

			outline:function ()
			{
				if($("#wrapper").hasClass("glossary") || $("#wrapper").hasClass("material") || $("#wrapper").hasClass("labs") || $("#wrapper").hasClass("options") || $("#wrapper").hasClass("mentoring"))
				{
					$("#wrapper").toggleClass("outline");
				}
				else
				{
					$("#wrapper").toggleClass("toggled-left").toggleClass("outline");
				}
				$("#wrapper").removeClass("toggled-right glossary material labs options mentoring");
			},

			glossary:function ()
			{
				if($("#wrapper").hasClass("outline") || $("#wrapper").hasClass("material") || $("#wrapper").hasClass("labs") || $("#wrapper").hasClass("options") || $("#wrapper").hasClass("mentoring"))
				{
					$("#wrapper").toggleClass("glossary");
				}
				else
				{
					$("#wrapper").toggleClass("toggled-left").toggleClass("glossary");
				}
				$("#wrapper").removeClass("toggled-right outline material labs options mentoring");
			},

			material:function ()
			{
				if($("#wrapper").hasClass("outline") || $("#wrapper").hasClass("glossary") || $("#wrapper").hasClass("labs") || $("#wrapper").hasClass("options") || $("#wrapper").hasClass("mentoring"))
				{
					$("#wrapper").toggleClass("material");
				}
				else
				{
					$("#wrapper").toggleClass("toggled-left").toggleClass("material");
				}
				$("#wrapper").removeClass("toggled-right outline glossary labs options mentoring");
			},

			labs:function ()
			{
				if($("#wrapper").hasClass("outline") || $("#wrapper").hasClass("glossary") || $("#wrapper").hasClass("options") || $("#wrapper").hasClass("material") || $("#wrapper").hasClass("mentoring"))
				{
					$("#wrapper").toggleClass("labs");
				}
				else
				{
					$("#wrapper").toggleClass("toggled-left").toggleClass("labs");
				}
				$("#wrapper").removeClass("toggled-right outline glossary material options mentoring");
			},
			
			mentoring:function ()
			{
				if($("#wrapper").hasClass("outline") || $("#wrapper").hasClass("glossary") || $("#wrapper").hasClass("options") || $("#wrapper").hasClass("material") || $("#wrapper").hasClass("labs"))
				{
					$("#wrapper").toggleClass("mentoring");
				}
				else
				{
					$("#wrapper").toggleClass("toggled-left").toggleClass("mentoring");
				}
				$("#wrapper").removeClass("toggled-right outline glossary material options labs");
			}
		},
		
		warning:
		{
			instance:'',
			count:0,
			step:0,
			duration:9,
			init:function()
			{
				ui.warning.interval(function()
				{
					ui.loader("show", function()
					{
						$("#wrapper").attr("data-remain","00:0"+ui.warning.duration).append("<div id='continue-course-btn'><a href='javascript:ui.warning.close();' class='cd-btn main-action'>CONTINUE</a></div>");
						ui.warning.step = 1;
						ui.warning.interval(function()
						{
							window.open("session_expire.html","_self");
						},ui.warning.duration);
					},"session");
				},5);
			},
			
			interval:function(callBack,duration)
			{
				ui.warning.clear();
				ui.warning.instance = setInterval(function()
				{
					if(ui.warning.step == 1)
					{
						//console.log(ui.warning.count + " > "+duration+" - "+(ui.warning.step == 1));
						$("#wrapper").attr("data-remain","00:0"+(ui.warning.duration-ui.warning.count));
					}
					
					if(ui.warning.count >= duration)
					{
						ui.warning.clear();
						if(typeof callBack == "function")
						{
							callBack();
						}
					}
					ui.warning.count++;
				},1000);
			},
			
			close:function()
			{
				if(ui.warning.step == 1)
				{
					ui.loader("hide", function()
					{
						
					},"session");
				}
				ui.warning.step = 0;
				$("#wrapper").removeAttr("data-remain");
				$("#wrapper #continue-course-btn").remove();
				
				ui.warning.clear();
			},
			
			clear:function()
			{
				ui.warning.count = 0;
				if(ui.warning.instance != '')
				{
					clearInterval(ui.warning.instance);
				}
			}
		},
		
		mouseAnalyzer:function (n)
		{
			switch(n){
				case 0:
					$('html').mouseleave(function() {
						//console.log('out');
						ui.nav.cache = $('#wrapper').attr('class');
						$('#wrapper').removeClass('toggled-top toggled-left toggled-bottom');
						ui.mouseAnalyzer(1);
					});
					ui.mouseAnalyzer(1);
				break;
				case 1:
					$('html').mouseover(function() {
						//console.log('over');
						$('#wrapper').addClass(ui.nav.cache);
						ui.nav.cache = '';
						$('html').unbind('mouseover');
					});
				break;
			}
		},
	
		svgModal:
		{
			coverLayer:'',
			duration:0,
			epsilon:0,
			aniObject:'',
			pathSteps:{},
			pathsArray:{},
			init: function (modalTrigger)
			{
				ui.svgModal.coverLayer = $('.cd-cover-layer');
				ui.svgModal.duration = 600;
				ui.svgModal.epsilon = (1000 / 60 / ui.svgModal.duration) / 4;
				ui.svgModal.aniObject = ui.svgModal.bezier(.63,.35,.48,.92, ui.svgModal.epsilon);
				
				$('a[data-type="cd-modal-trigger"]').each(function(){
					
					var modalTriggerId = (typeof $(this).attr('data-group') == "undefined"? $(this).attr('id'):$(this).attr('data-group'))/*$(this).attr('id')*/,
						modal = $('.cd-modal[data-modal="'+ modalTriggerId +'"]'),
						svgCoverLayer = modal.children('.cd-svg-bg'),
						paths = svgCoverLayer.find('path');
						ui.svgModal.pathsArray[modalTriggerId] = [];
					
					//store Snap objects
					ui.svgModal.pathsArray[modalTriggerId][0] = Snap('#'+paths.eq(0).attr('id')),
					ui.svgModal.pathsArray[modalTriggerId][1] = Snap('#'+paths.eq(1).attr('id')),
					ui.svgModal.pathsArray[modalTriggerId][2] = Snap('#'+paths.eq(2).attr('id'));

					//store path 'd' attribute values	
					ui.svgModal.pathSteps[modalTriggerId] = [];
					ui.svgModal.pathSteps[modalTriggerId][0] = svgCoverLayer.data('step1');
					ui.svgModal.pathSteps[modalTriggerId][1] = svgCoverLayer.data('step2');
					ui.svgModal.pathSteps[modalTriggerId][2] = svgCoverLayer.data('step3');
					ui.svgModal.pathSteps[modalTriggerId][3] = svgCoverLayer.data('step4');
					ui.svgModal.pathSteps[modalTriggerId][4] = svgCoverLayer.data('step5');
					ui.svgModal.pathSteps[modalTriggerId][5] = svgCoverLayer.data('step6');
					
					//open modal window
					$(this).on('click', function(event){
						event.preventDefault();
						ui.svgModal.open(this);
					});
				});
			},
			
			open:function(trg)
			{
				ui.isoTope.destroy(false);
				var $trg = $(trg)
				var id = (typeof $trg.attr('data-group') == "undefined"? $trg.attr('id'):$trg.attr('data-group'));
				var modal = $('.cd-modal[data-modal="'+ id +'"]');
				modal.addClass('modal-is-visible');
				ui.svgModal.coverLayer.addClass('modal-is-visible');
				ui.svgModal.animateModal(ui.svgModal.pathsArray[id], ui.svgModal.pathSteps[id], ui.svgModal.duration, 'open');
				svg_modal_open($trg);
			},
			
			close:function(id)
			{
				ui.isoTope.destroy(ui.isoTope.atConclude);
				var modal = $('.cd-modal[data-modal="'+ id +'"]');
				modal.removeClass('modal-is-visible');
				ui.svgModal.coverLayer.removeClass('modal-is-visible');
				ui.svgModal.animateModal(ui.svgModal.pathsArray[id], ui.svgModal.pathSteps[id], ui.svgModal.duration, 'close');
				svg_modal_close(id,modal);
				if($("#course_rec").hasClass("iso-main"))
				{
					_conclude_isotope();
				}
			},
			
			animateModal: function (paths, pathSteps, duration, animationType)
			{
				var path1 = ( animationType == 'open' ) ? pathSteps[1] : pathSteps[0],
					path2 = ( animationType == 'open' ) ? pathSteps[3] : pathSteps[2],
					path3 = ( animationType == 'open' ) ? pathSteps[5] : pathSteps[4];
				paths[0].animate({'d': path1}, duration, ui.svgModal.aniObject);
				paths[1].animate({'d': path2}, duration, ui.svgModal.aniObject);
				paths[2].animate({'d': path3}, duration, ui.svgModal.aniObject);
			},

			bezier: function (x1, y1, x2, y2, epsilon)
			{
				var curveX = function(t){
					var v = 1 - t;
					return 3 * v * v * t * x1 + 3 * v * t * t * x2 + t * t * t;
				};

				var curveY = function(t){
					var v = 1 - t;
					return 3 * v * v * t * y1 + 3 * v * t * t * y2 + t * t * t;
				};

				var derivativeCurveX = function(t){
					var v = 1 - t;
					return 3 * (2 * (t - 1) * t + v * v) * x1 + 3 * (- t * t * t + 2 * v * t) * x2;
				};

				return function(t){

					var x = t, t0, t1, t2, x2, d2, i;

					// First try a few iterations of Newton's method -- normally very fast.
					for (t2 = x, i = 0; i < 8; i++){
						x2 = curveX(t2) - x;
						if (Math.abs(x2) < epsilon) return curveY(t2);
						d2 = derivativeCurveX(t2);
						if (Math.abs(d2) < 1e-6) break;
						t2 = t2 - x2 / d2;
					}

					t0 = 0, t1 = 1, t2 = x;

					if (t2 < t0) return curveY(t0);
					if (t2 > t1) return curveY(t1);

					// Fallback to the bisection method for reliability.
					while (t0 < t1){
						x2 = curveX(t2);
						if (Math.abs(x2 - x) < epsilon) return curveY(t2);
						if (x > x2) t0 = t2;
						else t1 = t2;
						t2 = (t1 - t0) * .5 + t0;
					}

					// Failure
					return curveY(t2);
				};
			}
		
		},
		
		isoTope:
		{
			id:'',
			scrollElm:'',
			container:null,
			lastFilter:'*',
			loading:false,
			isStamp:false,
			init:function(elmId,id,isStamp)
			{
				isStamp = isStamp || false;
				ui.isoTope.isStamp = isStamp;
				
				if(isStamp)
				{
					$("#iso-filter-btns").children().each(function(i)
					{
						$(this).click(function(e){
							e.preventDefault();
							var filter = $(this).attr('data-filter');
							$(this).addClass("active");
							$("button[data-filter='"+ui.isoTope.lastFilter+"']").removeClass("active");
							ui.isoTope.lastFilter = filter;
							ui.isoTope.container.isotope({ filter: filter });
						});
					});
				}
				
				ui.isoTope.scrollElm = ".modal-is-visible > .cd-modal-content";
				$(ui.isoTope.scrollElm).on("scroll",ui.isoTope.scrollHandle);
				
				ui.isoTope.id = id;
				
				var instance = ui[ui.isoTope.id];
				instance.pageNumber = 1;
				instance.totalPages = 1;
				ui.isoTope.container = $("#"+elmId+" .iso-main").html(" ").isotope(instance.isoTopeConfig());
				ui.isoTope.getData();
			},
			scrollHandle:function(e)
			{
				if(ui.isoTope.loading){return;}
				var el = e.target;
				//  At Bottom
				if(el.offsetHeight-el.scrollHeight+el.scrollTop>-15)
				{
					ui.isoTope.loading = true;
					ui.isoTope.getData();
				}
			},
			getData:function ()
			{
				$trg = $(ui.isoTope.scrollElm).find(".load-more > a");
				if(!$trg.hasClass('elm-loader'))
				{
					$trg.addClass('elm-loader');
					ui[ui.isoTope.id].get(function(data){
						$trg.removeClass('elm-loader');
						ui.isoTope.getIsotope(data);
					});
				}
			},
			getIsotope:function(data)
			{
				var elems = [];
				var imgs = [];
				switch(ui.isoTope.id)
				{
					case 'relatedCourses':
						for(var i=0; i<data.length; i++)
						{
							var tempArr = ['','new','courses','packages','subs'];
							var itemCategory = tempArr[Math.floor(Math.random() * (3 - 0 + 1)) + 0];

							var $elem;
							if(i == 0 && ui.isoTope.isStamp)
							{
								var strHtml = 	'<div class="iso-item dual">'+
													'<div class="iso-image" style="background-image:url(assets/img/bannerSpotImage1.jpg)"></div>'+
													'<div class="iso-content">'+
														'<h1>Enhance Your Skillset With Our related Courses Offer!</h1>'+
														'<p>Take advantage of any of these related courses today, and save an additional 10% by using coupon code</p>'+
														'<div class="iso-coupon">"RELATEDCOURSE10"</h4>'+
													'</div>'+
												'</div>';

								$elem = $(strHtml);
								elems.push($elem[0]);
							}
							//else
							{
								var strHtml = 	'<div class="iso-item single '+itemCategory+'">'+
													'<div class="front">'+
														'<a href="'+data[i].StoreAddress+'" target="_blank" class="learn-more"></a>'+
														'<div class="iso-image" style="background-image:url('+data[i].CourseImageUrl+')"></div>'+
														'<div class="iso-title">'+data[i].CourseName+'</div>'+
														'<ul>'+
														  '<li>'+
															'Course Delivery: Online'+
														  '</li>'+
														  '<li>'+
															'Credit Hours: 60.00'+
														  '</li>'+
														  '<li>'+
															'Credit Type: Pre-License'+
														  '</li>'+
														'</ul>'+
														'<div class="price">$39.00</div>'+
														'<div class="iso-rate" data-reviews="333">'+
															'<i class="glyphicon glyphicon-star"></i>'+
															'<i class="glyphicon glyphicon-star"></i>'+
															'<i class="glyphicon glyphicon-star"></i>'+
															'<i class="glyphicon glyphicon-star-empty"></i>'+
															'<i class="glyphicon glyphicon-star-empty"></i>'+
															'<span class="'+itemCategory+'">'+itemCategory+'</span>'+
														'</div>'+
													'</div>'+
												'</div>';

								$elem = $(strHtml);
							}
							elems.push($elem[0]);
						}
					break;
					case 'clipp':
						for(var i=0; i<data.length; i++)
						{
							if(data[i].category == 'null')
							{
								data[i].category = 1;
							}
							
							var $elem;
							var strHtml = '';
							var isoClass = 'default';
							if(data[i].category == 1 && data[i].sourceImage != null)
							{
								//	With Image
								isoClass = 'with-image';
							}
							else if(data[i].category == 2)
							{
								//	With Video
								isoClass = 'with-video';
							}
							
								strHtml += 	'<div class="iso-clipp '+isoClass+'">'+
												'<div class="iso-provider-title">';
												
								if(data[i].favicon != null)
								{
									strHtml += 	'<img src="'+data[i].favicon+'">';
								}
								strHtml +=	data[i].siteName+'</div>';
								
								if(data[i].category == 1 && data[i].sourceImage != null)
								{
									imgs.push('<img class="source-image" src="'+data[i].sourceImage+'">');
									strHtml +=	'<div class="elm-loader-dum"><div class="source-image" style="background-image:url('+data[i].sourceImage+')"></div></div>';
								}
								
								if(data[i].category == 2)
								{
									var videoPortal = 'youtube';
									switch(videoPortal)
									{
										case "youtube":
											var countArg = String(data[i].link).split('=');
											var tempURL = String(data[i].link).split('?v=');
											if(countArg.length > 2)
											{
												tempURL[1] += "?"+tempURL[1];
											}
											tempURL = 'https://www.youtube.com/embed/'+tempURL[1];
											strHtml		+=	'<iframe scrolling="no" width="100%" height="200" frameborder="0" allowfullscreen src="'+tempURL+'"></iframe>';
										break;
										default:
											//strHtml	+=	'<div class="iso-video-title">'+data[i].title+'</div>'+
														//'<a class="iso-video" href="'+data[i].link+'" title="'+data[i].title+'" target="_blank"></a>';
									}
								}
								else
								{
									strHtml	+=	'<div class="iso-content">'+
													'<h5>'+data[i].title+'</h5>'+
													'<div class="iso-desc">'+data[i].short_description+'</div>'+
													'<br><small><a href="'+data[i].link+'" target="_blank">Read more.</a></small>'+
												'</div>';
								}
								
								strHtml	+= '</div>';

							$elem = $(strHtml);
							elems.push($elem[0]);
						}
					break;
				}
				ui.isoTope.item.add(elems);
				ui.isoTope.loading = false;
				/*ui.imageLoader.check(imgs,function(){
					$('.iso-clipp .elm-loader').removeClass('elm-loader');
					ui.isoTope.container.isotope();
				});*/
			},
			item:
			{
				add:function(elm){
					ui.isoTope.container.isotope('insert', elm);
				},
				close:function(elm){
					ui.isoTope.container.isotope( 'remove', $(elm).parent() ).isotope('layout');
				}
			},
			destroy:function()
			{
				if(ui.isoTope.container != null)
				{
					$(ui.isoTope.scrollElm).off("scroll");
					ui.isoTope.container.isotope('destroy');
					ui.isoTope.container = null;
				}
			}
		},
		
		imageLoader:
		{
			check:function(imagesArr,callBack)
			{
				$('body').append('<div id="temp-images-for-check" class="hide">'+imagesArr+'</div>');
				$('#temp-images-for-check').imagesLoaded( function() {
					$(this).remove();
					callBack();
				});
			}
		},
		
		slide:
		{
			intervalObj:'',
			sceneDuration:0,
			counter:0,
			coverLayer:'',
			duration:300,
			epsilon:0,
			aniObject1:'',
			aniObject2:'',
			svgPath:'',
			pathArray:[],
			path4:'',
			path5:'',
			init:function()
			{
				ui.slide.epsilon = (1000 / 60 / ui.slide.duration) / 4;
				ui.slide.aniObject1 = ui.svgModal.bezier(.42,.03,.77,.63, ui.slide.epsilon);
				ui.slide.aniObject2 = ui.svgModal.bezier(.27,.5,.6,.99, ui.slide.epsilon);
				
				ui.slide.coverLayer = $('#svg-loader-bg');
				var pathId = ui.slide.coverLayer.find('path').attr('id');
				ui.slide.svgPath = Snap('#'+pathId);
				
				ui.slide.pathArray[0] = ui.slide.coverLayer.data('step1');
				ui.slide.pathArray[1] = ui.slide.coverLayer.data('step6');
				ui.slide.pathArray[2] = ui.slide.coverLayer.data('step2');
				ui.slide.pathArray[3] = ui.slide.coverLayer.data('step7');
				ui.slide.pathArray[4] = ui.slide.coverLayer.data('step3');
				ui.slide.pathArray[5] = ui.slide.coverLayer.data('step8');
				ui.slide.pathArray[6] = ui.slide.coverLayer.data('step4');
				ui.slide.pathArray[7] = ui.slide.coverLayer.data('step9');
				ui.slide.pathArray[8] = ui.slide.coverLayer.data('step5');
				ui.slide.pathArray[9] = ui.slide.coverLayer.data('step10');
			},
			
			transition:function(template)
			{
				switch(template)
				{
					case "lesson_intro_typing":
						$('#page-content-wrapper .cd-intro-content').addClass("mask");
						$('#page-content-wrapper .content-wrapper').removeClass("fade");
					break;
					case "lesson_conclude_typing":
						$('#page-content-wrapper .cd-conclude-content').addClass("mask");
						$('#page-content-wrapper .content-wrapper').removeClass("fade");
					break;
				}
			},
			
			loader:{
				hide:function(callBack)
				{
					$('body').removeClass("static-loader");
					callBack();
					/*
					if(ui.slide.coverLayer.hasClass('loader'))
					{
						ui.slide.coverLayer.removeClass('loader');
						ui.slide.svgPath.animate({'d': ui.slide.path4}, ui.slide.duration, ui.slide.aniObject1, function(){
							ui.slide.svgPath.animate({'d': ui.slide.path5}, ui.slide.duration, ui.slide.aniObject2, function(){
								ui.slide.coverLayer.removeClass('is-animating');
								
								callBack();
								
							});
						});
					}*/
				},
				show:function(callBack)
				{
					$('body').addClass("static-loader");
					callBack();
				}
			},
			
			dummyLoad:function(template,callBack)
			{
				$("#page-content-wrapper > .wrapper-body").load("templates/"+template+".html", function()
				{
					if(typeof callBack == "function")
					{
						callBack();
					}
				});
			},
			
			prev:function(callBack)
			{
				ui.slide.loader.show(function(){
					callBack();
				});
				
				//ui.slide.coverLayer.addClass('loader');
				
				/*var path1 = ui.slide.pathArray[0],
					path2 = ui.slide.pathArray[2],
					path3 = ui.slide.pathArray[4];
				ui.slide.path4 = ui.slide.pathArray[6];
				ui.slide.path5 = ui.slide.pathArray[8];
				
				ui.slide.coverLayer.addClass('is-animating');
				
				ui.slide.svgPath.attr('d', path1);
				ui.slide.svgPath.animate({'d': path2}, ui.slide.duration, ui.slide.aniObject1, function(){
					ui.slide.coverLayer.addClass('loader');
					ui.slide.svgPath.animate({'d': path3}, ui.slide.duration, ui.slide.aniObject2, function(){
						
						callBack();
						
					});
				});*/
			},
			
			next:function(callBack)
			{
				ui.slide.loader.show(function(){
					callBack();
				});
				
				//ui.slide.coverLayer.addClass('loader');
				
				/*var path1 = ui.slide.pathArray[1],
					path2 = ui.slide.pathArray[3],
					path3 = ui.slide.pathArray[5];
				ui.slide.path4 = ui.slide.pathArray[7];
				ui.slide.path5 = ui.slide.pathArray[9];
				
				ui.slide.coverLayer.addClass('is-animating');
				
				ui.slide.svgPath.attr('d', path1);
				ui.slide.svgPath.animate({'d': path2}, ui.slide.duration, ui.slide.aniObject1, function(){
					ui.slide.coverLayer.addClass('loader');
					ui.slide.svgPath.animate({'d': path3}, ui.slide.duration, ui.slide.aniObject2, function(){
						
						callBack();
						
					});
				});*/
			},
			
			timer:function(LENGTH)
			{
				if(ui.slide.intervalObj != "")
				{
					clearInterval(ui.slide.intervalObj);
					ui.slide.intervalObj = "";
				}
				
				ui.slide.counter = 0;
				ui.slide.sceneDuration = LENGTH;
				
				var $sd = $("#scene-duration");
				var $bar = $sd.find(".progress-bar");
				var $label = $sd.find("label");
				$bar.css("width","100%");
				$label.text("00:0"+(ui.slide.sceneDuration-ui.slide.counter));
				
				$sd.addClass('slideIn');
				
				ui.slide.intervalObj = setInterval(function(){
					
					if(ui.slide.counter >= ui.slide.sceneDuration)
					{
						$sd.removeClass('slideIn');
						clearInterval(ui.slide.intervalObj);
						ui.slide.intervalObj = "";
					}
					else
					{
						ui.slide.counter++;
						$bar.css("width",((((ui.slide.sceneDuration-ui.slide.counter) / ui.slide.sceneDuration)) * 100)+"%");
						$label.text("00:0"+(ui.slide.sceneDuration-ui.slide.counter));
					}
					
				},1000);
			}
		},
		
		loader:function(cond,callBack,state)
		{
			var $body = $('body');
			if(cond == "show")
			{
				$body.addClass("pre-loader done up");
				
				setTimeout(function(){
					$body.removeClass("done up");
					setTimeout(function(){
						$body.addClass(state);
					},500);
					setTimeout(function(){
						callBack();
					},1500);
				},500);
			}
			else if(cond == "hide")
			{
				$body.addClass("done");
				setTimeout(function(){
					$body.addClass("up");
					setTimeout(function(){
						$body.removeClass("pre-loader done up "+state);
						callBack();
					},500);
				},500);
			}
		},
		
		guide:
		{
			trg:{tourWrapper:'',tourSteps:'',stepsNumber:''},
			init:function()
			{
				ui.guide.trg.tourWrapper = $('.cd-tour-wrapper');
				ui.guide.trg.tourSteps = ui.guide.trg.tourWrapper.children('li');
				ui.guide.trg.stepsNumber = ui.guide.trg.tourSteps.length;
				var tourStepInfo = $('.cd-more-info');

				//create the navigation for each step of the tour
				ui.guide.createNav(ui.guide.trg.tourSteps, ui.guide.trg.stepsNumber);
				
				$('#cd-tour-trigger').on('click', function(){
					
					//	BEGIN - MODAL BOX -------------------------------
					var $trgModal = $("#dynamicModal");
				
					//	BEGIN TITLE, MESSAGE AND BUTTONS
					var title = 'Welcome To The New Course Player';
					var msg = "<p class='guide-content'>This guided tour will walk you through all the new functionality, look, and feel included in the new course player experience.</p>";
					var btns = '<button type="button" class="cd-btn main-action" onclick="ui.guide.begin();" data-dismiss="modal">Start Tutorial</button>';
					//	END TITLE, MESSAGE AND BUTTONS
					
					$trgModal.find(".modal-title").html(title);
					$trgModal.find(".modal-body").html(msg);
					$trgModal.find(".modal-footer").html(btns);
					
					$trgModal.modal('show');
					//	END - MODAL BOX --------------------------------
					
				});

				//change visible step
				tourStepInfo.on('click', '.cd-prev', function(event){
					//go to prev step - if available
					( !$(event.target).hasClass('inactive') ) && ui.guide.changeStep(ui.guide.trg.tourSteps, 'prev');
				});
				tourStepInfo.on('click', '.cd-next', function(event){
					//go to next step - if available
					if($(event.target).hasClass('end'))
					{
						//	BEGIN - MODAL BOX -------------------------------
						var $trgModal = $("#dynamicModal");
					
						//	BEGIN TITLE, MESSAGE AND BUTTONS
						var title = "That's It. Start Learning.";
						var msg = "<p class='guide-content'>In an effort to keep the course player as simple as possible, we've created an easy to use interface for your learning experience. We hope you enjoy it.</p>";
						var btns = '<button type="button" class="cd-btn main-action" data-dismiss="modal">Close Tour</button>';
						//	END TITLE, MESSAGE AND BUTTONS
						
						$trgModal.find(".modal-title").html(title);
						$trgModal.find(".modal-body").html(msg);
						$trgModal.find(".modal-footer").html(btns);
						
						$trgModal.modal('show');
						ui.guide.close(ui.guide.trg.tourSteps, ui.guide.trg.tourWrapper);
						//	END - MODAL BOX --------------------------------
					}
					else
					{
						(!$(event.target).hasClass('inactive') ) && ui.guide.changeStep(ui.guide.trg.tourSteps, 'next');
					}
				});

				//close tour
				tourStepInfo.on('click', '.cd-close', function(event){
					ui.guide.close(ui.guide.trg.tourSteps, ui.guide.trg.tourWrapper);
				});
			},
			
			begin:function()
			{
				//start tour
				if(!ui.guide.trg.tourWrapper.hasClass('active')) {
					//in that case, the tour has not been started yet
					ui.guide.trg.tourWrapper.addClass('active');
					ui.guide.showStep(ui.guide.trg.tourSteps.eq(0));
				}
			},

			createNav:function(steps, n)
			{
				var tourNavigationHtml = '<div class="cd-nav"><span><b class="cd-actual-step">1</b> of '+n+'</span><ul class="cd-tour-nav"><li><a href="javascript:;" class="cd-prev">&#171; Previous</a></li><li><a href="javascript:;" class="cd-next">Next &#187;</a></li></ul></div><a href="javascript:;" class="cd-close"></a>';

				steps.each(function(index){
					var step = $(this),
						stepNumber = index + 1,
						nextClass = ( stepNumber < n ) ? '' : 'inactive',
						prevClass = ( stepNumber == 1 ) ? 'inactive' : '';
					var nav = $(tourNavigationHtml).find('.cd-next').addClass(nextClass).end().find('.cd-prev').addClass(prevClass).end().find('.cd-actual-step').html(stepNumber).end().appendTo(step.children('.cd-more-info'));
				});
			},

			showStep:function(step)
			{
				switch(step.attr("id")){
					case "guide-menu":
						$("#wrapper").removeClass("toggled-left");
					break;
					case "guide-menu-expend":
						$("#wrapper").addClass("toggled-left");
					break;
					case "guide-time-spent":
						step.find(".cd-next.inactive").removeClass("inactive").addClass("end");
					break;
				}
				step.addClass('is-selected').removeClass('move-left');
				ui.guide.smoothScroll(step.children('.cd-more-info'));
			},

			smoothScroll:function(element)
			{
				(element.offset().top < $(window).scrollTop()) && $('body,html').animate({'scrollTop': element.offset().top}, 100);
				(element.offset().top + element.height() > $(window).scrollTop() + $(window).height() ) && $('body,html').animate({'scrollTop': element.offset().top + element.height() - $(window).height()}, 100); 
			},

			changeStep:function(steps, bool)
			{
				var visibleStep = steps.filter('.is-selected'),
					delay = (ui.guide.viewportSize() == 'desktop') ? 300: 0; 
				visibleStep.removeClass('is-selected');

				(bool == 'next') && visibleStep.addClass('move-left');

				setTimeout(function(){
					( bool == 'next' )
						? ui.guide.showStep(visibleStep.next())
						: ui.guide.showStep(visibleStep.prev());
				}, delay);
			},

			close:function(steps, wrapper)
			{
				steps.removeClass('is-selected move-left');
				wrapper.removeClass('active');
			},

			viewportSize:function()
			{
				/* retrieve the content value of .cd-main::before to check the actua mq */
				return window.getComputedStyle(document.querySelector('.cd-tour-wrapper'), '::before').getPropertyValue('content').replace(/"/g, "").replace(/'/g, "");
			}
		},
		
		social:
		{
			title:"",
			specificTitle:"",
			desc:"",
			url:"",
			img:"",
			
			in:{
				init:function()
				{
					IN.Event.on(IN, "auth", ui.social.in.share);
                },
				
				share:function(caseNum)
				{
					var obj = {
						url: ui.social.url,
						title:ui.social.heading(caseNum),
						summary: ui.social.desc,
						source: ui.social.url
					}
					window.open('http://www.linkedin.com/shareArticle?mini=true&url='+obj.url+'&title='+obj.title+'&summary='+obj.summary+'&source='+obj.source+'', "", "width=600,height=400");
					
					//IN.UI.Share().params(obj).place();
				}

			},
			
			fb:
			{
				key:'',
				init: function(){
				  window.fbAsyncInit = function() {
					FB.init({
					  appId      : ui.social.fb.key,
					  xfbml      : true,
					  version    : 'v2.9'
					});
				  };

				  (function(d, s, id){
					 var js, fjs = d.getElementsByTagName(s)[0];
					 if (d.getElementById(id)) {return;}
					 js = d.createElement(s); js.id = id;
					 js.src = "//connect.facebook.net/en_US/sdk.js";
					 fjs.parentNode.insertBefore(js, fjs);
				   }(document, 'script', 'facebook-jssdk'));
				},
				
				share:function(caseNum)
				{
					FB.ui({
						method: 'feed',
						app_id: ui.social.fb.key,
						name:ui.social.heading(caseNum),
						link: ui.social.url,
						picture: ui.social.img,
						caption: "To find more details, click on this post",
						description: ui.social.desc
					},
					function(response){
						
					});
				}
			},
			
			heading: function(n)
			{
				switch(n)
				{
					case 1:
						return ui.social.title;
					break;
					case 2:
						return "I am learning lesson " + ui.social.specificTitle+' in the course '+ui.social.title + ' at Quickstart.com';
					break;
					case 3:
						return 'Course Completed! I completed the course, "'+ui.social.title + '", on Quickstart.com';
					break;
				}
			},
			
			click:function(e)
			{
				ui.social.specificTitle = $(e).data("title");
				ui.svgModal.open($("<a data-group='modal-dynamic' data-trg='social-sharing'></a>"));
			}
		},
		discussions:
		{
			contract:{
				'user': {
					'username':"test13@test13.com",
					'email':"test13@test13.com"
				},
				'password': {
					'newPassword': "test9",
					'newPasswordConfirm': "test9"
				},
				'profile': {
					'firstname': "Test13",
					'lastname': "Test13"
				},
				'space': {
					'name': "Using WMI and CIM",
					'description':"This course teaches you how you can use the CIM cmdlets and PowerShell to manage local and remote systems.",
					'guid': "b47df2a3-99fb-4cf8-a4c9-008fd3dc03d9"
				},
				'access_token':"U6UgT88XLUvUolAP5WuYJFO1"
			},
			domain:"https://community.quickstart.com",	//https://community.quickstart.com, http://10.0.102.42/humhub2, humhub.360training.com
			isEnable:true,
			spaceUsers:{},
			uId:1,
			pId:0,
			ccId:0,
			shouldBlink:false,
			cache:"",
			init:function()
			{
				if($("html").hasClass("no-touch"))
				{
					//	Desktop Specific
					$('#notificationList .scrollable').slimscroll({
						height: '400px',
						color: '#fff',
						wheelStep: 5
					});
				}
				else
				{
					//	Mobile Specific
					$('#notificationList .scrollable').slimscroll({
						height: '400px',
						color: '#fff'
					});
				}

				$('.notifications').on('click', '.dropdown-menu', function(){
					$(this).closest('.dropup').addClass('dontClose');
				});
				$('.notifications > .dropup').on('hide.bs.dropdown', function(e){
					if ( $(this).hasClass('dontClose') ){
						e.preventDefault();
					}
					$(this).removeClass('dontClose');
				});
				$("#notificationList > .visitSpace").attr("href",ui.discussions.domain+"/s/"+ui.discussions.contract.space.guid+"/");
			},

			createSpace:function($thisModal)
			{
				var vhtml = $.parseHTML(ui.discussions.contract.space.description);
				ui.discussions.contract.space.description = $(vhtml).text();

				var apiPath = ui.discussions.domain+"/index.php/api/quickstart/enrol-user/?access_token="+ui.discussions.contract.access_token;
				$.ajax({
					type:"POST",
					url:apiPath,
					data:{
						User:ui.discussions.contract.user,
						Password:ui.discussions.contract.password,
						Profile:ui.discussions.contract.profile,
						Space:ui.discussions.contract.space
					},
					success: function(data){
						//ui.discussions.contract.csrfToken = data.csrfToken;
						var requestURL = ui.discussions.domain + "/index.php/user/auth/auto-login/?a="+btoa(ui.discussions.contract.user.username+"|360.quickstart.360@humhub.com|"+ui.discussions.contract.space.guid);

						var newWin = window.open(requestURL,"_blank");
						$thisModal.removeClass('pre-loader please-wait');
						if(!newWin || newWin.closed || typeof newWin.closed=='undefined')
						{
							$thisModal.html("<h1>Popup Blocked</h1><p>We attempted to launch discussion forum in a new window, but a popup blocker is preventing it from opening. Please disable popup blockers for this site.</p>");
						}
						else
						{
							ui.svgModal.close('modal-dynamic');
						}
						return;

						//	Return Place above because store of the task changed after all developments.
						ui.discussions.color = data.Space.color;
						ui.discussions.uId = data.User.id;
						$thisModal.html("").load("templates/disscussions.html", function()
						{
							$thisModal.removeClass('pre-loader please-wait');
						});
					},
					fail: function(){
						console.log('Error: ' + response.responseText);
					}
				});
			},

			full:function(trg)
			{
				if(ui.discussions.isEnable)
				{
					ui.discussions.contract.space.name = $(trg).data("label");
					ui.discussions.contract.space.description = "";
					ui.discussions.contract.space.guid = $(trg).data("guid");
					
					ui.svgModal.open($('<a data-group="modal-dynamic" data-trg="discussions" data-type="cd-modal-trigger"></a>'));
				}
				else
				{
					//	BEGIN - MODAL BOX -------------------------------
					var $trgModal = $("#dynamicModal");
					$trgModal.find(".modal-title").html("");
					$trgModal.find(".modal-body").html("").load("assets/templates/disscussion-enable.html");
					$trgModal.find(".modal-footer").html("");
					$trgModal.modal('show');
					//	END - MODAL BOX --------------------------------
				}
			}
		},

		clipp:
		{
			courseName:'',
			courseGuid:'',
			searchBy:'',
			pageSize:20,
			pageNumber:1,
			totalPages:1,
			domain:'https://clipp.quickstart.com',
			open:function(e)
			{
				ui.svgModal.open($('<a data-group="modal-dynamic" data-trg="clipp" data-type="cd-modal-trigger"></a>'));
			},
			load:function($thisModal)
			{
				$thisModal.html("").load("assets/templates/clipp.html", function()
				{
					$thisModal.removeClass('pre-loader please-wait');
				});
			},
			init:function()
			{
				$("#clippSearchBy").val(ui.clipp.searchBy);
				$("#clippContentFilter").html("<br><p class='text-center'>Content filter is loading, please wait..</p>").parent().on('hide.bs.dropdown', function () {
					if(JSON.stringify(ui.clipp.contentFilter.updateList)!=JSON.stringify(ui.clipp.contentFilter.list))
					{
						ui.storage.set(ui.discussions.contract.user.username+ui.discussions.contract.space.guid+"clippCF", JSON.stringify(ui.clipp.contentFilter.updateList));
						ui.clipp.open();
					}
				});
				$("#clippConceptCloud").html("<br><p class='text-center'>Concept cloud is loading, please wait..</p>").parent().on('hide.bs.dropdown', function () {
					if(JSON.stringify(ui.clipp.conceptCloud.updateList)!=JSON.stringify(ui.clipp.conceptCloud.list))
					{
						ui.storage.set(ui.discussions.contract.user.username+ui.discussions.contract.space.guid+"clippCC", JSON.stringify(ui.clipp.conceptCloud.updateList));
						ui.clipp.open();
					}
				});
				$('.clipp-header').on('click', '.dropdown-menu.hold-on-click', function (e) {
					e.stopPropagation();
				});
			},
			isoTopeConfig:function()
			{
				ui.clipp.init();
				return ({
					itemSelector: '.iso-clipp',
					masonry: {
						columnWidth: 362
					}
				});
			},
			get:function(callBack)
			{
				if(ui.clipp.pageNumber > ui.clipp.totalPages)
				{
					callBack([]);
				}
				else
				{
					//  Begin - Content Filter
					var CFData = ui.storage.get(ui.discussions.contract.user.username+ui.discussions.contract.space.guid+"clippCF");
					if(CFData !== null)
					{
						ui.clipp.contentFilter.updateList = ui.clipp.contentFilter.list = JSON.parse(CFData);
					}
					else
					{
						// For First time
						ui.clipp.contentFilter.updateList = ui.clipp.contentFilter.list = [{"label":"payscale","selected":true},{"label":"github","selected":true},{"label":"arsTech","selected":true},{"label":"linkedin","selected":true},{"label":"youtube","selected":true},{"label":"vimeo","selected":true},{"label":"issuu","selected":true},{"label":"indeed","selected":true}];
					}
					ui.clipp.contentFilter.init();
					//  End - Content Filter

					var bodyData = {
						"courseName": ui.clipp.courseName,
						"courseGuid": ui.clipp.courseGuid,
						"pageSize": ui.clipp.pageSize,
						"pageNumber": ui.clipp.pageNumber,
						"searchBy": ui.clipp.searchBy,
						"conceptCloud": ui.clipp.conceptCloud.updateList,
						"contentFilter": ui.clipp.contentFilter.updateList
					};

					var dontSubmitCCData = (ui.clipp.conceptCloud.updateList == undefined);
					if(!dontSubmitCCData)
					{
						dontSubmitCCData = (ui.clipp.conceptCloud.updateList.length == 0);
					}
					if(dontSubmitCCData)
					{
						delete bodyData.conceptCloud;
					}

					var apiPath = ui.clipp.domain+"/api/content";
					$.ajax({
						type: 'POST',
						dataType:'json',
						headers: {
							'Accept': 'application/json',
							'Content-Type': 'application/json'
						},
						url: apiPath,
						data: JSON.stringify(bodyData),
						success: function(data)
						{
							//  Begin - Concept Cloud
							var CCData = ui.storage.get(ui.discussions.contract.user.username+ui.discussions.contract.space.guid+"clippCC");
							if(CCData !== null)
							{
								ui.clipp.conceptCloud.updateList = ui.clipp.conceptCloud.list = JSON.parse(CCData);
							}
							else
							{
								ui.clipp.conceptCloud.updateList = ui.clipp.conceptCloud.list = data.conceptCloud;
							}
							var dontShowCC = (ui.clipp.conceptCloud.updateList == undefined);
							if(!dontShowCC)
							{
								dontShowCC = (ui.clipp.conceptCloud.updateList.length == 0);
							}
							if(dontShowCC)
							{
								$("#clippConceptCloud").parent().addClass("hide");
							}
							else
							{
								$("#clippConceptCloud").parent().removeClass("hide");
							}
							ui.clipp.conceptCloud.init();
							//  End - Concept Cloud

							ui.clipp.pageNumber = data.pageNumber+1;
							ui.clipp.totalPages = data.totalPages;
							callBack(data.result);

							if(data.pageNumber === data.totalPages)
							{
								$('.load-more').addClass('hide');
							}
						},
						fail: function()
						{
							console.log('Error: ' + response.responseText);
						}
					});
				}
			},
			conceptCloud:
			{
				updateList:[],
				list:[],
				init:function()
				{
					var listHtml = "";
					var isAllTrue = true;
					for(var i=0; i<(ui.clipp.conceptCloud.list == undefined?0:ui.clipp.conceptCloud.list.length);i++)
					{
						if(isAllTrue)
						{
						  isAllTrue = ui.clipp.conceptCloud.list[i].selected;
						}
						listHtml += '<li>'+
										'<label class="dark-item" data-val="'+i+'">'+
											'<div class="input-group">'+
											  '<span>'+ui.clipp.conceptCloud.list[i].label+'</span>'+
											  '<span class="input-group-addon">'+
												'<input data-label="'+ui.clipp.conceptCloud.list[i].label+'" onclick="ui.clipp.checkboxes(this,false,\'clippConceptCloud\',\'conceptCloud\')" type="checkbox" '+(ui.clipp.conceptCloud.list[i].selected?"checked ":"")+'>'+
												'<i></i>'+
											  '</span>'+
											'</div>'+
										'</label>'+
									'</li>';
					}
					var htmlHead = '<li><h2 class="text-center">Concept <span class="primary-font">Cloud</span></h2></li>'+
										'<li>'+
											'<label class="dark-item">'+
												'<div class="input-group">'+
												  '<span>Display All Results</span>'+
												  '<span class="input-group-addon">'+
													'<input class="checkAll" id="clippConceptCloudCheckAll" type="checkbox" '+(isAllTrue?"checked ":"")+' onclick="ui.clipp.checkboxes(this,true,\'clippConceptCloud\',\'conceptCloud\')">'+
													'<i></i>'+
												  '</span>'+
												'</div>'+
											'</label>'+
										'</li>';
					$("#clippConceptCloud").html(htmlHead+listHtml);
				}
			},
			contentFilter:
			{
				updateList:[],
				list:[],
				init:function()
				{
					var listHtml = "";
					var isAllTrue = true;
					for(var i=0; i<(ui.clipp.contentFilter.list == undefined?0:ui.clipp.contentFilter.list.length);i++)
					{
						if(isAllTrue)
						{
						  isAllTrue = ui.clipp.contentFilter.list[i].selected;
						}
						listHtml += '<li>'+
									'<label class="dark-item" data-val="'+i+'">'+
										'<div class="input-group">'+
										  '<img src="assets/img/clipp/'+ui.clipp.contentFilter.list[i].label+'.svg">'+
										  '<span class="input-group-addon">'+
											'<input data-label="'+ui.clipp.contentFilter.list[i].label+'" onclick="ui.clipp.checkboxes(this,false,\'clippContentFilter\',\'contentFilter\')" type="checkbox" '+(ui.clipp.contentFilter.list[i].selected?"checked ":"")+'>'+
											'<i></i>'+
										  '</span>'+
										'</div>'+
									'</label>'+
								'</li>';
					}
					var htmlHead = '<li><h2 class="text-center">Content <span class="primary-font">Filter</span></h2></li>'+
												'<li>'+
													'<label class="dark-item">'+
														'<div class="input-group">'+
														  '<span>Display All Content Providers</span>'+
														  '<span class="input-group-addon">'+
															'<input class="checkAll" id="clippContentFilterCheckAll" type="checkbox" '+(isAllTrue?"checked ":"")+' onclick="ui.clipp.checkboxes(this,true,\'clippContentFilter\',\'contentFilter\')">'+
															'<i></i>'+
														  '</span>'+
														'</div>'+
													'</label>'+
												'</li>';
					$("#clippContentFilter").html(htmlHead+listHtml);
				}
			},
			checkboxes:function(trg,isBase,allCheckbox,trgModule)
			{
				var checkArr = [];
				if(isBase)
				{
					$(trg).parent().parent().parent().parent().parent().children().each(function()
					{
						var checked = $(trg).prop('checked');
						var checkBox = $(this).find("input[type='checkbox']");
						checkBox.prop({"checked": checked});
						if(!checkBox.hasClass("checkAll") && typeof checkBox.prop("checked") != 'undefined')
						{
							checkArr.push({"label":checkBox.attr('data-label'),"selected":checked});
						}
					});
				}
				else
				{
					var check = true;
					$(trg).parent().parent().parent().parent().parent().children().each(function()
					{
						var checkBox = $(this).find("input[type='checkbox']");
						if(!checkBox.hasClass("checkAll") && typeof checkBox.prop("checked") != 'undefined')
						{
							var checked = checkBox.prop("checked");
							checkArr.push({"label":checkBox.attr('data-label'),"selected":checked});
							if(!checked)
							{
								check = false;
							}
						}
					});
					$("#"+allCheckbox+"CheckAll").prop({"checked":check});
				}

				ui.clipp[trgModule].updateList = checkArr;
			},
			search:function(e)
			{
				if(typeof(e) !== "undefined")
				{
				  var key = e.which || e.keyCode;

						// Enter is pressed
						if (key == 13)
				  {
					//  On Key Enter
					ui.clipp.searchBy = $("#clippSearchBy").val();
					ui.clipp.open();
				  }
				}
				else
				{
				  //  On Button Click
				  ui.clipp.searchBy = $("#clippSearchBy").val();
				  ui.clipp.open();
				}
			}
		},
		labs:
		{
			domain:"http://10.0.100.102:8081",
			key:btoa("lod|password1"),
			init:function()
			{
				$("#labs-course-action").attr("action",ui.labs.domain);
				$("#labs-access-key").val(ui.labs.key);
			},
			load:function($thisModal)
			{
				$thisModal.html("").load("assets/templates/labs.html", function()
				{
					$thisModal.removeClass('pre-loader please-wait');
				});
			},
			open:function(e)
			{
				ui.svgModal.open($('<a data-group="modal-dynamic" data-trg="labs" data-type="cd-modal-trigger"></a>'));
			}
		},
		storage:
		{
		  get:function(name)
		  {
			if (typeof(Storage) !== "undefined")
			{
			  return (sessionStorage.getItem(name));
			}
			return false;
		  },
		  set:function(name,value)
		  {
			if (typeof(Storage) !== "undefined")
			{
			  sessionStorage.setItem(name, value);
			}
		  }
		},
		
		relatedCourses:
		{
			pageSize:20,
			pageNumber:1,
			totalPages:0,
			domain:'#',
			isoTopeConfig:function()
			{
				return ({
					itemSelector: '.iso-item',
					getSortData:{
						category: '[data-category]'
					},
					masonry:{
					  columnWidth: 242
					}
				});
			},
			get:function(callBack)
			{
				var data = {
						"affiliateItems": [
						{
						  "CourseName": "Abdus Classroom Case3 and Case5 Combine DIFFERENT LOCATION",
						  "CourseGuid": "f2aa2c26490247faadfe3b5705711920",
						  "CourseImageUrl": "assets/img/bannerSpotImage1.jpg",
						  "StoreAddress": "http://www.360training.com"
						}
					  ]
					};
				callBack(data.affiliateItems);
			}
		},
		
		uploadFile:function(e,id)
		{
			if (e.files && e.files[0])
			{
				var reader = new FileReader();
				reader.onload = function (trg){
					$('#'+id).removeClass("defualt").addClass("preview");
					$('#'+id+' > img').attr('src', trg.target.result);
				}
				reader.readAsDataURL(e.files[0]);
			}
		}
	}
}();
