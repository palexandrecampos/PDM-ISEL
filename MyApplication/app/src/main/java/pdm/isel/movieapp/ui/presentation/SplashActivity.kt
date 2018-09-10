package pdm.isel.movieapp.ui.presentation

import android.content.Intent
import android.os.Bundle
import android.app.Activity
import android.os.Handler
import kotlinx.android.synthetic.main.activity_splash.*
import pdm.isel.movieapp.R


class SplashActivity : Activity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val secondsDelayed = 3
        Handler().postDelayed({
            splashscreen.setImageResource(R.drawable.movies_logo)
            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            finish()
        }, (secondsDelayed * 1000).toLong())
    }
}