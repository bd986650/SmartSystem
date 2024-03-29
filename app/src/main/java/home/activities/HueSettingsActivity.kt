package home.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.smartsystem.R
import home.adapters.SimpleListAdapter
import home.api.HueAPI
import home.data.SimpleListItem
import home.helpers.Devices
import home.helpers.Theme
import home.interfaces.RecyclerViewHelperInterface
import org.json.JSONObject

class HueSettingsActivity : AppCompatActivity(), RecyclerViewHelperInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        Theme.set(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devices)

        val devices = Devices(this)
        val id = intent.getStringExtra("device") ?: ""
        if (!devices.idExists(id)) {
            finish()
            return
        }

        val device = devices.getDeviceById(id)
        val hueAPI = HueAPI(this, device.id)
        val addressPrefix = device.address + "api/" + hueAPI.getUsername()
        val queue = Volley.newRequestQueue(this)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val items = arrayListOf<SimpleListItem>()
        items.add(
            SimpleListItem(
                device.name,
                device.address,
                icon = device.iconId
            )
        )
        queue.add(
            JsonObjectRequest(
                Request.Method.GET, "$addressPrefix/sensors", null,
                { response ->
                    val sensorItems = arrayListOf<SimpleListItem>()
                    for (i in response.keys()) {
                        val current = response.optJSONObject(i) ?: JSONObject()
                        val config = current.optJSONObject("config") ?: JSONObject()
                        if (config.has("battery")) {
                            sensorItems.add(
                                SimpleListItem(
                                    current.optString("name"),
                                    config.optString("battery") + "%",
                                    icon = if (config.optBoolean("reachable")) R.drawable.ic_device_raspberry_pi
                                    else R.drawable.ic_warning
                                )
                            )
                        }
                    }
                    items.add(
                        SimpleListItem(summary = resources.getString(R.string.hue_controls))
                    )
                    sensorItems.sortBy { it.title }
                    items.addAll(sensorItems)

                    queue.add(
                        JsonObjectRequest(
                            Request.Method.GET, "$addressPrefix/lights", null,
                            { innerResponse ->
                                val lightItems = arrayListOf<SimpleListItem>()
                                for (i in innerResponse.keys()) {
                                    val current = innerResponse.optJSONObject(i) ?: JSONObject()
                                    val state = current.optJSONObject("state") ?: JSONObject()
                                    lightItems.add(
                                        SimpleListItem(
                                            current.optString("name"),
                                            (if (state.optBoolean("on")) resources.getString(R.string.str_on)
                                            else resources.getString(R.string.str_off))
                                                    + " · "
                                                    + current.optString("productname"),
                                            icon = if (state.optBoolean("reachable")) R.drawable.ic_device_lamp
                                            else R.drawable.ic_warning
                                        )
                                    )
                                }
                                items.add(
                                    SimpleListItem(summary = resources.getString(R.string.hue_lights))
                                )
                                lightItems.sortBy { it.title }
                                items.addAll(lightItems)
                                recyclerView.adapter = SimpleListAdapter(items, this)
                            }, { }
                        )
                    )
                }, { }
            )
        )
    }

    override fun onItemClicked(view: View, position: Int) {}
}
