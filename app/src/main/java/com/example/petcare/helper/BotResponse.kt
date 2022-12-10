package com.example.petcare.helper

import com.example.petcare.data.remote.response.BotDisease
import com.example.petcare.data.remote.response.BotQuestion

object BotResponse {

    fun basicResponses(_message: String): String {
        val random = (0..2).random()
        val closing = "Thankyou for using Pet Care Application chat bot. If you wish to repeat everything from the beginning, type \"RESET\""
        val message =_message.lowercase()
        val questionListDogs = listOf(
            BotQuestion("G01", "Does the dog's hair falling out?"),
            BotQuestion("G02", "Does dog skin crust?"),
            BotQuestion("G03", "Does the dog have a bad smell"),
            BotQuestion("G04", "Has the dog's appetite decreased?"),
            BotQuestion("G05", "Is the dog losing weight??"),
            BotQuestion("G06", "Does the dog look lethargic?"),
            BotQuestion("G07", "Is the dog's hair falling out?"),
            BotQuestion("G08", "Is the dog's hair falling out?"),
            BotQuestion("G09", "Is the dog's hair falling out?"),
            BotQuestion("G10", "Is the dog's hair falling out?")

        )
        val questionList = listOf(
            BotQuestion("G01", "Does your bird have fluid in the eyes?"),
            BotQuestion("G02", "Does swelling occur in the eye area?"),
            BotQuestion("G03", "Let's start the consultation with the condition of your lovebird's eyes. First thing first, Does your bird's eye look cloudy?"),
            BotQuestion("G04", "The next stage is to see the state of the poop from the birds. Is the poop Liquid poop?"),
            BotQuestion("G05", "Is the bird's poop white like chalk?"),
            BotQuestion("G06", "Is the bird's poop white like chalk?"),
            BotQuestion("G07", "Look at your bird's belly, does the stomach look enlarged?"),
            BotQuestion("G08", "Look at your bird's feathers, do they look damaged?"),
            BotQuestion("G09", "Does the lovebird's chest hair expand?"),
            BotQuestion("G10", "Does the fur look like there are fleas?"),
            BotQuestion("G11", "Look at the bird's nose, does the nose look runny?"),
            BotQuestion("G12", "Does the bird's legs look swollen?"),
            BotQuestion("G13", "Do the bird's hooves look elongated?"),
            BotQuestion("G14", "Look at your bird's feet, the bird's legs look limp?"),
            BotQuestion("G15", "Does your bird often shake its head?")
        )

        val diseaseList = listOf(
            BotDisease("P01", "Your lovebird has the possibility of Corzya Disease", "Coryza is an infectious disease in poultry that attacks the respiratory system and is caused by bacteria. The disease is usually acute to subacute and usually becomes chronic in its progress. This disease is characterized by inflammation of the catarrh of the mucous membranes of the upper respiratory tract (nasal cavity, infraobial sinuses and upper trachea).","Treatment of a flock with sulfonamides or antibiotics is recommended. Various kinds of sulfonamides such as sulfadimethoxine, sulfaquinoxaline, sulfamethazine are all effective, but sulfadimethoxine is the safest drug. Treatment through drinking water will give a quick response. While the use of antibiotics that are beneficial include using tetracycline, erythromycin, spectinomycin and tylosin, where their use is relatively safe and effective for poultry."),
            BotDisease("P02", "Your lovebird has the possibility of Digestive Diseases/Diarrhea", "Diarrhea is an increase in frequency and liquid content of the fecal component of the droppings. In birds, the droppings are composed of three elements: feces, urates and urine. The droppings are stored in the cloaca, the common emptying chamber for the gastrointestinal, urinary and reproductive tracts.", "Treatment for diarrhea may include any combination of:" +
                    "\n" +
                    "Hospitalization for fluids and injectable medications\n" +
                    "Antibiotics or antifungal medications\n" +
                    "Surgery or endoscopy to relieve intestinal obstructions\n" +
                    "Medications to protect the intestinal tract or alter the motility of the intestinal tract"),
            BotDisease("P03", "Your lovebird has the possibility of Nyilet Disease", "In some cases, the bird's appetite is still good, but the body remains thin and narrow-chested. In various incidents, lovebirds with a narrow chest that are left without treatment end in death. If the dead are superior sires, of course this is a big loss.", "Generally, lovebird whose body was thin or nyilet breasted caused errors in care, especially the lack of nutrients and vitamins. It could also be because the worms that enter the body of the bird, then multiply in the digestive tract and sucking most of even whole food juices that should be distributed to all body tissues."),
            BotDisease("P04", "Your lovebird has the possibility of Egg Binding Disease", "Egg binding occurs when the female bird is unable to expel an egg from her body. While most female birds have no problems laying eggs, occasionally they may encounter difficulty. When detected early, egg binding may be resolved easily. If a prolonged period has elapsed since a bird began attempting to lay an egg, she may become critically ill.", "Treatment varies depending upon how sick the bird is, the location of the egg, and the length of time the bird has been egg bound. Critically ill birds are first treated for shock, and then attempts are made to extract the egg. Mildly affected birds may respond to supplemental heat, rehydration with injectable fluids, calcium, and vitamin D3. \n " +
                    "If the egg is near the cloacal opening, your veterinarian may be able to gently extract it with cotton swabs and medical lubricant. Eggs that do not pass with these therapies require more aggressive therapy. Your veterinarian may need to pass a needle through the vent and into the eggshell to aspirate (draw out) the contents of the egg, causing the shell to collapse. Following this treatment, your veterinarian may pull out the empty shell, or it may be left to pass out of the bird within a few days. If your veterinarian cannot see the egg through the vent, surgery under general anesthetic may be necessary to remove the egg from the abdomen."),
            BotDisease("P05", "Your lovebird has the possibility of Pullorum disease", "Pullorum is a disease that affects many poultry, both chickens and birds. Usually caused by salmonella or pullorum which have infectious properties, starting from the air, direct contact (touch) or food or drink.", "Treatment is indeed very much done, but not all drugs will give maximum and satisfactory results. But treatment will be able to inhibit mortality and reduce or reduce the levels of lime diarrhea. Usually the drugs to treat chalky stools in poultry and birds are therapy, medoxy, sulfamix, choleridine, teteacholer, respiratek, neo meditril and so on. Make an election to treat or deal with chalky stools depending on the breeder choosing which drug to use, usually after giving the drug give back 4-5 days of vita stress to relieve stress and help restore the bird's body condition."),
            BotDisease("P06", "Your lovebird has the possibility of Tick disease", "Tick that attack birds are a type of ectoparasites, namely parasites that live outside the host's body (in this case birds). Generally, tick attacks do not cause real death, but the presence of these ticks will cause the bird to feel uncomfortable. When attacking, tick will live on the skin and feathers of birds so that birds will often peck at their feathers and even cause injuries.", "Treatment can be done in the following ways:\n" +
                    "1. Feather cleaning\n" +
                    "2. Give tick medicine\n" +
                    "3. Treat wounds\n" +
                    "4. Bird cage cleaning"),
            BotDisease("P07", "Your lovebird has the possibility of Respiratory disease", "Respiratory diseases are among the most common problems seen in all species of pet birds. Because they can have a variety of causes, early diagnosis by your veterinarian and proper treatment is necessary to prevent severe illness.", "We recommend you take your bird to the nearest clinic. Once the correct diagnosis is made, your veterinarian may suggest a course of antibiotics if the problem is a bacterial infection. Oral or nebulized (aerosolized) antifungal drugs are used to treat fungal disease, such as aspergillosis, and oral or injectable anti-parasitic drugs are used to treat parasitic infections, such as air sac mites. Improper diets are slowly corrected, and vitamin supplementation is used if vitamin A deficiency is suspected. Seriously-ill birds are hospitalized, so that injectable and aerosolized medications can be used, and force-feeding and IV fluids can be administered, if needed. Early diagnosis and appropriate therapy are key to successful treatment of birds with respiratory tract disease."),
            BotDisease("P08","Your lovebird has the possibility of Bubul disease", "Bubul disease is a type of disease that often attacks almost all types of songbirds, especially canaries. The cause of bubul disease is a disease caused by Staphylo coccus bacteria which usually attacks the skin which is generally located on the soles of the feet. If this is left unchecked and it gets worse, the songbird will be disabled, and of course this disease will spread to the other leg and to other birds.", "Treatment can be done by:\n" +
                    "1. Make sure the environment is clean, air circulation is smooth, and there is enough lighting.\n" +
                    "2. Thoroughly clean the lovebird cage with disinfectant, including the part of the tangkringan where the lovebird's feet are always attached.\n" +
                    "3. Wash your lovebird's feet with warm water, repeat until the warts on the soles of their feet are soft, dry them with a clean cloth.\n" +
                    "4. Apply anti-ointment drugs such as Daktarin, Canesten, Betason N, or Ointment 88. Do this regularly 2 times a day.\n" +
                    "5. The sign of healing is that the warts on his feet are drying and peeling."),
            BotDisease("P09","Your lovebird has the possibility of Tetelo disease", "Lovebird tetelo disease tends to attack the bird's nervous system, causing the affected bird to experience paralysis and lead to death if it doesn't get treatment quickly.", "Treatment can be done by:\n" +
                    "1. Give warm sugar water or real honey.\n" +
                    "2. Natural medicine can be given from ingredients: papaya leaves, temureng, temulawak, garlic and onion peels, tea leaves, cassava leaves, bay leaves which are boiled together, filtered, and given to lovebirds.\n" +
                    "3. Avoid bathing until the lovebird is healed, keep the environment clean, the cage and eat and drink."),
        )

        return when {

            message.contains("a0") -> {
                "${questionList[2].question} \n a1 : yes \n a2 : no"
            }

            message == "a1" -> {
                "${questionListDogs[0].question} \n a1 : yes \n a2 : no"
            }

            message == "a3" -> {
                "${questionList[1].question} \n a5 : yes \n a6 : no"
            }

            message == "a4" || message == "a6" -> {
                "${questionList[6].question} \n a7 : yes \n a8 : no"
            }

            message == "a7" -> {
                "${questionList[7].question} \n a9 : yes \n a10 : no"
            }

            message == "a8" -> {
                "${questionList[8].question} \n a13 : yes \n a14 : no"
            }

            message == "a9" -> {
                "${questionList[13].question} \n a11 : yes \n a12 : no"
            }

            message == "a12" -> {
                "${questionList[3].question} \n a15 : yes \n a16 : no"
            }

            message == "a13" -> {
                "${questionList[10].question} \n a19 : yes \n a20 : no"
            }

            message == "a15" -> {
                "${questionList[4].question} \n a17 : yes \n a18 : no"
            }

            message == "a20" -> {
                "${questionList[13].question} \n a21 : yes \n a22 : no"
            }

            message == "a21" -> {
                "${questionList[14].question} \n a23 : yes \n a24 : no"
            }

            message == "a2" || message == "a14"-> {
                "${questionList[3].question} \n a25 : yes \n a26 : no"
            }

            message == "a25" || message == "a18"-> {
                "${questionList[8].question} \n a27 : yes \n a28 : no"
            }

            message == "a28" || message == "a34"-> {
                "${questionList[6].question} \n a29 : yes \n a30 : no"
            }

            message == "a26"-> {
                "${questionList[7].question} \n q31 : yes \n q32 : no"
            }

            message == "a31" || message == "a32"-> {
                "${questionList[8].question} \n a33 : yes \n a34 : no"
            }

            message == "a33"-> {
                "${questionList[9].question} \n a35 : yes \n a36 : no"
            }

            message == "a36"-> {
                "${questionList[11].question} \n a37 : yes \n a38 : no"
            }

            message == "a37"-> {
                "${questionList[12].question} \n a39 : yes \n a40 : no"
            }

            message == "a39"-> {
                "${diseaseList[7].opening}\n\n${diseaseList[7].explanation}\n\n${diseaseList[7].medication}\n\n${closing}"
            }

            message == "a35"-> {
                "${diseaseList[5].opening}\n\n${diseaseList[5].explanation}\n\n${diseaseList[5].medication}\n\n${closing}"
            }

            message == "a29"-> {
                "${diseaseList[2].opening}\n\n${diseaseList[2].explanation}\n\n${diseaseList[2].medication}\n\n${closing}"
            }


            message == "a27"-> {
                "${diseaseList[1].opening}\n\n${diseaseList[1].explanation}\n\n${diseaseList[1].medication}\n\n${closing}"
            }

            message == "a23" -> {
                "${diseaseList[8].opening}\n\n${diseaseList[8].explanation}\n\n${diseaseList[8].medication}\n\n${closing}"
            }

            message == "a19" -> {
                "${diseaseList[6].opening}\n\n${diseaseList[6].explanation}\n\n${diseaseList[6].medication}\n\n${closing}"
            }


            message == "a17" -> {
                "${diseaseList[4].opening}\n\n${diseaseList[4].explanation}\n\n${diseaseList[4].medication}\n\n${closing}"
            }

            message == "a5" -> {
                "${diseaseList[0].opening}\n\n${diseaseList[0].explanation}\n\n${diseaseList[0].medication}\n\n${closing}"
            }

            message == "a11" -> {
                "${diseaseList[3].opening}\n\n${diseaseList[3].explanation}\n\n${diseaseList[3].medication}\n\n${closing}"
            }

            message == "a10" || message == "a16" || message == "a22" || message == "a24" || message == "a30" ||
                    message == "a38" || message == "a40" -> {
                "You have reached the end of lovebird consultation, It seems we can't find your bird's disease. If the symptoms persist, we suggest taking your bird to the nearest veterinary clinic, thank you."
            }

            else -> {
                when (random) {
                    0 -> "I don't understand..."
                    1 -> "Try asking me something different"
                    2 -> "Please answer according to the instruction"
                    else -> "error"
                }
            }
        }
    }
}