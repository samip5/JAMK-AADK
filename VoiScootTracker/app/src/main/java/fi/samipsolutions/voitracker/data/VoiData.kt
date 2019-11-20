package fi.samipsolutions.voitracker.data

data class VoiData(
	val added: String? = null,
	val bounty: Int? = null,
	val type: String? = null,
	val battery: Int? = null,
	val modelSpecification: Any? = null,
	val zone: Int? = null,
	val serial: Any? = null,
	val name: String? = null,
	val jsonMemberShort: String? = null,
	val location: List<Double?>? = null,
	val id: String? = null,
	val locked: Boolean? = null,
	val updated: String? = null,
	val registrationPlate: String? = null,
	val status: String? = null,
	val mileage: Int? = null
)
