package dev.samip.golfcoursewishlist

class Places {

    companion object {

        var placeNameArray = arrayOf(
            "Black Mountain",
            "Chambers Bay",
            "Clear Water",
            "Harbour Town",
            "Muirfield",
            "Old Course",
            "Pebble Beach",
            "Spy Class",
            "Turtle Bay"
        )

        fun placeList(): ArrayList<Place> {
            val list = ArrayList<Place>()
            for (i in placeNameArray.indices) {
                val place = Place()
                place.name = placeNameArray[i]
                place.image = placeNameArray[i].replace("\\s+".toRegex(), "").toLowerCase()
                list.add(place)
            }
            return list
        }
    }
}