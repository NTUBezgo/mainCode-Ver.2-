$(function(){

  /**
   * Drag and Drop
   */
  var table = ["tableContent", "tableContent2", "tableContent3"];
  var course = ["course1", "course2"];
  var $dragging = null;
  $(document.body).on("mousemove", function(e) {
      if ($dragging) {
        $("body").addClass("-body-dragging");
        //console.log("ss");
        $dragging.offset({
            top: e.pageY + 5,
            left: e.pageX + 5
        });
      }
  });
  $("div.draggable").on("mousedown", function (e) {
    console.log("拖曳啟動");
    var nearest_li = $(e.target).closest("li");
    var create_dragging_el = $(nearest_li).find("div.draggable").clone().addClass("-dragging");
    $(nearest_li).append(create_dragging_el);
    $dragging = create_dragging_el;
  });
  $(document.body).on("mouseup", function (e) {
    if($dragging){
      /*if(e.target.id == "tableContent"){
      document.getElementById(table[1]).innerHTML=e.target.id;
      }
      */
      if(table.indexOf(e.target.id)+1){
        document.getElementById(table[table.indexOf(e.target.id)]).innerHTML=document.getElementById(course[0]).innerHTML;
      }else{
        $dragging.animate({
            top: 0,
            left: 0
        });
      }
      $("body").removeClass("-body-dragging");
      $($dragging).fadeOut(function(){ $(this).remove() });

      console.log("拖曳解除");
      $dragging = null;
    }


  });

});