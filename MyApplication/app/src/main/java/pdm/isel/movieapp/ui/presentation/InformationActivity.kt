package pdm.isel.movieapp.ui.presentation

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_information.*
import android.content.Intent
import android.net.Uri
import pdm.isel.movieapp.R


class InformationActivity : ToolbarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_information)

        logoImage.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = Uri.parse("https://www.themoviedb.org/")
            startActivity(intent)
        }
    }
}
