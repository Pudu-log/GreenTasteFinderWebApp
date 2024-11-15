<html>
<head>
  <title>Add a Map with Markers using HTML</title>
</head>
<body>
<div id="googleMap" style="width: 100%;height: 700px;"></div>

<script>
  let map;
  //apikey 싱글톤으로 빼는게 낫나?
  const apiKey = "AIzaSyDv0yF-dMGzUxSlJojgLQyWZ4xudsAUX2g";

  async function initMap() {
    //위치 선언
    const position = { lat: -25.344, lng: 131.031 };
    // Request needed libraries.
    //@ts-ignore
    const { Map } = await google.maps.importLibrary("maps");
    const { AdvancedMarkerElement } = await google.maps.importLibrary("marker");

    // The map, centered at Uluru
    map = new Map(document.getElementById("googleMap"), {
      zoom: 4,
      center: position,
      mapId: "MAP_ID",
    });

    // The marker, positioned at Uluru
    const marker = new AdvancedMarkerElement({
      map: map,
      position: position,
      title: "이름 적기",
    });
  }

  const xhr = new XMLHttpRequest();
  const url = "https://maps.googleapis.com/maps/api/place/details/json?place_id=ChIJ70lL5f4iZDURou4DxhPonPA&fields=formatted_address%2Cname%2Crating%2Cgeometry&key=AIzaSyDv0yF-dMGzUxSlJojgLQyWZ4xudsAUX2g";
  xhr.onload = function () {
    console.log(this.response);
  }
  xhr.open("GET", url);
  xhr.send();

</script>
<%--구글맵 api 호출 및 initmap 작동--%>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDv0yF-dMGzUxSlJojgLQyWZ4xudsAUX2g&callback=initMap"></script>
</body>
</html>