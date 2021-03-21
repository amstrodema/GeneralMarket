package comgalaxyglotech.confirmexperts.generalmarket.Trash;

/**
 * Created by ELECTRON on 10/04/2019.
 */

public class RecipeMainModel {
    String id,creatorId;
    String recipeName,recipeLink,recipeProcedure,recipeSignature, recipeIngredients,recipeDesc;

    public RecipeMainModel() {
    }

    public RecipeMainModel(String id, String creatorId, String recipeName, String recipeLink, String recipeProcedure, String recipeSignature, String recipeIngredients, String recipeDesc) {
        this.id = id;
        this.creatorId = creatorId;
        this.recipeName = recipeName;
        this.recipeLink = recipeLink;
        this.recipeProcedure = recipeProcedure;
        this.recipeSignature = recipeSignature;
        this.recipeIngredients = recipeIngredients;
        this.recipeDesc = recipeDesc;
    }

    public String getRecipeDesc() {
        return recipeDesc;
    }

    public void setRecipeDesc(String recipeDesc) {
        this.recipeDesc = recipeDesc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeLink() {
        return recipeLink;
    }

    public void setRecipeLink(String recipeLink) {
        this.recipeLink = recipeLink;
    }

    public String getRecipeProcedure() {
        return recipeProcedure;
    }

    public void setRecipeProcedure(String recipeProcedure) {
        this.recipeProcedure = recipeProcedure;
    }

    public String getRecipeSignature() {
        return recipeSignature;
    }

    public void setRecipeSignature(String recipeSignature) {
        this.recipeSignature = recipeSignature;
    }

    public String getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(String recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }
}
