package io.github.sceneview.sample.armodelviewer
//List of packages/libraries used for Superimposition project
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.utils.setFullScreen
//Bottom import is used for error handling
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.gorisse.thomas.lifecycle.getActivity

/*
SceneView 1.0.0 is necessary for rendering 3D objects, It is imported from an example that allows users
to place a 3D object on the screen and allow for depth occlusion, which will allow the shoe to appear
to wrap around the ankle. The "arsceneview" library was utilized completely, and the "sample-ar-
model-viewer" was used to developed the superimposition subsystem. The code was modified to meet
the needs of the design, as well as researched on to fully understand the code.

Author: Thomas Gorisse
Date: arsceneview 9/29/2022
      sample-ar-viewer 9/29/2022
Site: https://github.com/SceneView/sceneview-android

 */

//creates Class activity for AR instance named ARcam
class ARcam : AppCompatActivity(R.layout.activity) {
    //create variables for actions within the AR instance:
    //sceneView aids with rendering 3D visuals on a screen
    //loadingView aids with loading 3D object files
    lateinit var sceneView: ArSceneView
    lateinit var loadingView: View

    //Create variable for creating an alert box
    private lateinit var builder : AlertDialog.Builder
    private lateinit var builder2 : AlertDialog.Builder

    //function that will handle scenarios where the shoe file cannot be found
    fun fileNotFound() {
        builder = AlertDialog.Builder(this)
        //title of the dialogue box
        builder.setTitle("Alert!")
             //output desired message to the user stating that the shoe file is not available
            .setMessage("Sorry, the shoe you are looking for is currently unavailable. We apologize for the inconvenience. Contact ShoeAR regarding any questions.")
            //setup button for the user to click
            .setPositiveButton("Ok"){dialogInterface, it->
                //finish the activity to exit back into the shoe details menu
                finish()
            }
            .setCancelable(false)
            //show the dialogue box
            .show()
    }
    //function for starting message upon opening the AR camera
    fun startUp() {
        builder2 = AlertDialog.Builder(this)
        //title of the dialogue box
        builder2.setTitle("Notice!")
            //output desired message to the user stating instructions on how to use the AR camera
            //for now, the application works with the left foot only
            .setMessage("Please use your left foot when trying on shoes. The shoe will be automatically placed on any flat surface and adjust to lighting. Use it at your best convenience!")
            //setup button for the user to click to continue
            .setPositiveButton("Ok"){dialogInterface, it->
                //move on to the AR camera instance
            }
            //show the dialogue box
            .setCancelable(false)
            .show()
    }

    //set up class named "Model" that will handle 3D characteristics
    data class Model(
        //file location
        val fileLocation: String,
        //scale (cm -> scaleUnit conversion requires dividing by 80)
        val scaleUnits: Float? = null,
        //standard placement method: follows a point on the screen
        val placementMode: PlacementMode = PlacementMode.BEST_AVAILABLE,
        //rotation (if applicable)
        val applyPoseRotation: Boolean = true
    ) //Models are loaded as follows: Model("file_path", scaleUnits, applyPoseRotation )

    //initialize node for where model will be placed
    var modelNode: ArModelNode? = null

    //error handling for loading object
    var isLoading = false
        set(value) {
            field = value
            loadingView.isGone = !value
        }

    //Function for creating a scene on opening the ARCam activity
    override fun onCreate(savedInstanceState: Bundle?) {
        startUp()
        super.onCreate(savedInstanceState)
        // Instructions for screenfitting and formatting
        setFullScreen(
            //Include/Exclude android phone features such as the toolbar
            findViewById(R.id.rootView),
            fullScreen = false,
            hideSystemBars = false,
            fitsSystemWindows = false
        )
        //Initializing the sceneView variable, necessary for rendering 3D shoe
        sceneView = findViewById(R.id.sceneView)
        //Initializing the loadingView variable, necessary for loading 3D shoe
        loadingView = findViewById(R.id.loadingView)

        //newModelNode initializes the node for model placement
        newModelNode()
    }


    //function to prepare another object
    //used after the ARCam instance is created
    fun newModelNode() {
        //.getStringExtra obtains the shoe model path of the current shoeID, or the shoe menu the
        //user is currently in. This is where Superimposition integrates with the Database by
        //pulling the appropriate shoe model
        val filename = intent.getStringExtra("EXTRA_FILENAME").toString()
        // the same is being done for the foot size. The foot size is passed as a string, and then converted
        // into an integer
        val Userfootsize = intent.getStringExtra("EXTRA_FOOTSIZE").toString()
        // Not necessary for the application, this is used to keep track what is the user's foot size being passed
        // on to the sample-ar-model-viewer
        Log.e("TEST", Userfootsize)
        // the foot size is converted into an integer in order to use it for scaling
        val footSize = Userfootsize.toInt() + 2.0f
        //Log.e("TEST", footSize.toString())
        //val footSize = 24f + 2.0f (SCRAP)
        //after taking measurements in a series of trials of measuring virtual shoes at different
        // scales, the unit of conversion between centimeters and sceneview scale is 80
        val scaleFoot = footSize / 80

        //"models/canguro.glb" example file path that filename represents
        // enabling applyPoseRotation fixes the objects rotation, meaning there would be a
        // limit view of the 3D model
        val model = Model(filename, scaleUnits = scaleFoot, applyPoseRotation = false)

        //detects when file has fully loaded
        isLoading = true
        modelNode?.takeIf { !it.isAnchored }?.let {
            sceneView.removeChild(it)
            it.destroy()
        }
        //val model = models[0]
        //function that will render in the 3D shoe model onto the user's screem
        modelNode = ArModelNode(model.placementMode).apply {
            //setting up transform function. Typically will be used to handle rotating a 3D object
            transform(
                //rotation that will initialize the shoe being pointed stright forward from the
                //user's perspective
                rotation = Rotation(y = 90.0f)
            )
            //applies the rotation listed above
            applyPoseRotation = model.applyPoseRotation
            //function used to load 3D model
            //load Model Async has numerous parametere. The main ones are glbFileLocation, scaleToUnits,
            // centerOrigin, and the newly added onError feature that will trigger if any parameter is broken
            // or missing
            loadModelAsync(
                //parameters for the current Kotlin file the AR model function is used
                context = this@ARcam,
                lifecycle = lifecycle,
                //passing file location from model
                glbFileLocation = model.fileLocation,
                //not needed, but included
                autoAnimate = true,
                // passing scale from model
                scaleToUnits = model.scaleUnits,
                // Place the model origin at the bottom center
                centerOrigin = Position(y = -1.0f),
                // Exit AR camera if the shoe file is not found
                //call the function fileNotFound()
                onError = { error: Exception -> fileNotFound() }
            ) {
                //detect plane for placing 3D object
                //change to true if you would like to see if planes are being detected
                // go to line 237 in PlaneRenderer.kt to see where planes are being generated
                sceneView.planeRenderer.isVisible = false
                //update loading variable
                isLoading = false
            }
        }
        // add model node
        sceneView.addChild(modelNode!!)
        // select model node. This acts as the base for the shoe model
        sceneView.selectedNode = modelNode
    }
    //A common bug was the inability to swtich back into the shoeDetailsFragment without crashing.
    //This function helps solve the issue temporarily.
    override fun onBackPressed() {
        getActivity()!!.onBackPressed()
    }
}