package com.example.cocktailbook.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "DBDrink")
data class Drink(
    @SerializedName("idDrink")
    @Expose
    @PrimaryKey
    var idDrink : String = "",

    @SerializedName("strDrink")
    @Expose
    @ColumnInfo(name = "strDrink")
    var strDrink: String = "",

    @SerializedName("strCategory")
    @Expose
    @ColumnInfo(name = "strCategory")
    var strCategory: String = "",

    @SerializedName("strAlcoholic")
    @Expose
    @ColumnInfo(name = "strAlcoholic")
    var strAlcoholic: String = "",

    @SerializedName("strGlass")
    @Expose
    @ColumnInfo(name = "strGlass")
    var strGlass: String = "",

    @SerializedName("strInstructions")
    @Expose
    @ColumnInfo(name = "strInstructions")
    var strInstructions: String? = null,

    @SerializedName("strDrinkThumb")
    @Expose
    @ColumnInfo(name = "strDrinkThumb")
    var strDrinkThumb: String? = null,

    @SerializedName("strIngredient1")
    @Expose
    @ColumnInfo(name = "strIngredient1")
    var strIngredient1: String? = null,

    @SerializedName("strIngredient2")
    @Expose
    @ColumnInfo(name = "strIngredient2")
    var strIngredient2: String? = null,

    @SerializedName("strIngredient3")
    @Expose

    @ColumnInfo(name = "strIngredient3")
    var strIngredient3: String? = null,

    @SerializedName("strIngredient4")
    @Expose
    @ColumnInfo(name = "strIngredient4")
    var strIngredient4: String? = null,

    @SerializedName("strIngredient5")
    @Expose
    @ColumnInfo(name = "strIngredient5")
    var strIngredient5: String? = null,

    @SerializedName("strIngredient6")
    @Expose
    @ColumnInfo(name = "strIngredient6")
    var strIngredient6: String? = null,

    @SerializedName("strMeasure1")
    @Expose
    @ColumnInfo(name = "strMeasure1")
    var strMeasure1: String? = null,

    @SerializedName("strMeasure2")
    @Expose
    @ColumnInfo(name = "strMeasure2")
    var strMeasure2: String? = null,

    @SerializedName("strMeasure3")
    @Expose
    @ColumnInfo(name = "strMeasure3")
    var strMeasure3: String? = null,

    @SerializedName("strMeasure4")
    @Expose
    @ColumnInfo(name = "strMeasure4")
    var strMeasure4: String? = null,

    @SerializedName("strMeasure5")
    @Expose
    @ColumnInfo(name = "strMeasure5")
    var strMeasure5: String? = null,

    @SerializedName("strMeasure6")
    @Expose
    @ColumnInfo(name = "strMeasure6")
    var strMeasure6: String? = null,
){

    @ColumnInfo(name = "ingredients")
    var ingredients: ArrayList<String> = ArrayList()
    @ColumnInfo(name = "measure")
    var measure: ArrayList<String> = ArrayList()


    fun allMeasure(){
        strMeasure1?.let { measure.add(it) }
        strMeasure2?.let { measure.add(it) }
        strMeasure3?.let { measure.add(it) }
        strMeasure4?.let { measure.add(it) }
        strMeasure5?.let { measure.add(it) }
        strMeasure6?.let { measure.add(it) }
    }

    fun allIngredients(){
        strIngredient1?.let { ingredients.add(it) }
        strIngredient2?.let { ingredients.add(it) }
        strIngredient3?.let { ingredients.add(it) }
        strIngredient4?.let { ingredients.add(it) }
        strIngredient5?.let { ingredients.add(it) }
        strIngredient6?.let { ingredients.add(it) }
    }


    override fun toString(): String {
        return "Drink(ingredients=$ingredients, idDrink='$idDrink', strDrink='$strDrink', strCategory='$strCategory', strAlcoholic='$strAlcoholic', strGlass='$strGlass', strInstructions=$strInstructions, strDrinkThumb=$strDrinkThumb, strIngredient1=$strIngredient1, strIngredient2=$strIngredient2, strIngredient3=$strIngredient3, strIngredient4=$strIngredient4, strIngredient5=$strIngredient5, strIngredient6=$strIngredient6, strMeasure1=$strMeasure1, strMeasure2=$strMeasure2, strMeasure3=$strMeasure3, strMeasure4=$strMeasure4, strMeasure5=$strMeasure5, strMeasure6=$strMeasure6)"
    }



}

/*
"idDrink": "11007",
"strDrink": "Margarita",
"strDrinkAlternate": null,
"strDrinkES": null,
"strDrinkDE": null,
"strDrinkFR": null,
"strDrinkZH-HANS": null,
"strDrinkZH-HANT": null,
"strTags": "IBA,ContemporaryClassic",
"strVideo": null,
"strCategory": "Ordinary Drink",
"strIBA": "Contemporary Classics",
"strAlcoholic": "Alcoholic",
"strGlass": "Cocktail glass",
"strInstructions": "Rub the rim of the glass with the lime slice to make the salt stick to it. Take care to moisten only the outer rim and sprinkle the salt on it. The salt should present to the lips of the imbiber and never mix into the cocktail. Shake the other ingredients with ice, then carefully pour into the glass.",
"strInstructionsES": null,
"strInstructionsDE": "Reiben Sie den Rand des Glases mit der Limettenscheibe, damit das Salz daran haftet. Achten Sie darauf, dass nur der \u00e4u\u00dfere Rand angefeuchtet wird und streuen Sie das Salz darauf. Das Salz sollte sich auf den Lippen des Genie\u00dfers befinden und niemals in den Cocktail einmischen. Die anderen Zutaten mit Eis sch\u00fctteln und vorsichtig in das Glas geben.",
"strInstructionsFR": null,
"strInstructionsZH-HANS": null,
"strInstructionsZH-HANT": null,
"strDrinkThumb": "https:\/\/www.thecocktaildb.com\/images\/media\/drink\/wpxpvu1439905379.jpg",
"strIngredient1": "Tequila",
"strIngredient2": "Triple sec",
"strIngredient3": "Lime juice",
"strIngredient4": "Salt",
"strIngredient5": null,
"strIngredient6": null,
"strIngredient7": null,
"strIngredient8": null,
"strIngredient9": null,
"strIngredient10": null,
"strIngredient11": null,
"strIngredient12": null,
"strIngredient13": null,
"strIngredient14": null,
"strIngredient15": null,
"strMeasure1": "1 1\/2 oz ",
"strMeasure2": "1\/2 oz ",
"strMeasure3": "1 oz ",
"strMeasure4": null,
"strMeasure5": null,
"strMeasure6": null,
"strMeasure7": null,
"strMeasure8": null,
"strMeasure9": null,
"strMeasure10": null,
"strMeasure11": null,
"strMeasure12": null,
"strMeasure13": null,
"strMeasure14": null,
"strMeasure15": null,
"strCreativeCommonsConfirmed": "No",
"dateModified": "2015-08-18 14:42:59"
}*/
