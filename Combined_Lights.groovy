/**
 *  Combined Lights
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
    name: "Combined Lights",
    namespace: "tchoward",
    author: "Thomas Howard",
    description: "Will set a Virtual Light based on any of the selected lights being triggered",
    category: "Security",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Select Contact Sensor Group") {
		input "switches", "capability.switch", title: "Light Switches", multiple: true, required: true
        input "virtualSwitch", "capability.switch", title: "Virtual Light Switch", multiple: false, required: true
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
	setSwitch()
	subscribe(switches, "switch", "switchHandler")
}

def switchHandler(evt) {
	setSwitch()
}

def setSwitch(){
	def onCounter = 0
    
    def currSwitches = switches.currentSwitch

    def onSwitches = currSwitches.findAll { switchVal ->
        switchVal == "on" ? true : false
    }
    
    log.debug "${onSwitches.size()} out of ${switches.size()} switches are on"
    
    if (onSwitches.size() > 0) {
    	if (virtualSwitch.currentState("switch") != "on") {
    		virtualSwitch.on()
            log.debug("Virtual Switch is On")
        }
    } else {
        if (virtualSwitch.currentState("switch") != "off") {
    		virtualSwitch.off()
            log.debug("Virtual Switch is Off")
        }
    }
}
