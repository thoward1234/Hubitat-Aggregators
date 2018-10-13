/**
 *  Combined Contact Sensors
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
    name: "Combined Contact Sensors",
    namespace: "tchoward",
    author: "Thomas Howard",
    description: "Will set a virtual sensor based on any of the selected sensors being triggered",
    category: "Security",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Select Contact Sensor Group") {
		input "contactSensors", "capability.contactSensor", title: "Contact Sensors", multiple: true, required: true
        input "virtualContact", "capability.contactSensor", title: "Virtual Contact Sensor", multiple: false, required: true
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
	setContact()
	subscribe(contactSensors, "contact", "contactHandler")
}

def contactHandler(evt) {
	setContact()
}

def setContact(){
	def openCounter = 0
    
    contactSensors.each {
    	if (it.currentValue("contact") == "open") {
        	openCounter++
        }
    }
    
    log.debug("Windows Open: ${openCounter}, virtualContact: ${virtualContact.currentValue("contact")}")
    
    if (openCounter > 0) {
    	if (virtualContact.currentValue("contact") != "open") {
    		virtualContact.open()
            log.debug("Window is Open")
        }
    } else {
        if (virtualContact.currentValue("contact") != "closed") {
    		virtualContact.close()
            log.debug("Window is Closed")
        }
    }
}
