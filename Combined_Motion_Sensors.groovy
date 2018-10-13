/**
 *  Combined Motion Sensors
 *
 *  Copyright 2018 Thomas Howard
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Combined Motion Sensors",
    namespace: "tchoward",
    author: "Thomas Howard",
    description: "Will set a virtual sensor based on any of the selected sensors being triggered",
    category: "Security",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Select Motion Sensor Group") {
		input "motionSensors", "capability.motionSensor", title: "Motion Sensors", multiple: true, required: true
        input "virtualMotion", "capability.motionSensor", title: "Virtual Motion Sensor", multiple: false, required: true
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	setMotion()
	subscribe(motionSensors, "motion", "motionHandler")
}

def motionHandler(evt) {
	setMotion()
}

def setMotion(){
	def motionCounter = 0
    
    motionSensors.each {
    	if (it.currentValue("motion") == "active") {
        	motionCounter++
        }
    }
    
    log.debug("Windows Open: ${motionCounter}, virtualMotion: ${virtualMotion.currentValue("motion")}")
    
    if (motionCounter > 0) {
    	if (virtualMotion.currentValue("motion") != "active") {
    		virtualMotion.active()
            log.debug("Motion is Active")
        }
    } else {
        if (virtualMotion.currentValue("motion") != "inactive") {
    		virtualMotion.inactive()
            log.debug("Motion is Inactive")
        }
    }
}
