package com.example.projectmobilecharactergenerator;

import java.util.Random;

public class VillainGenerator {
    public static String getScheme() {
        Random random = new Random();
        String[][] schemes = {
                {"Acquire a legendary item to prolong life", "Ascend to godhood", "Become undead or obtain a younger body", "Steal a planar creature's essence"},
                {"Seize a position of power or title ", "Win a contest or tournament", "Win favor with a powerful individual ", "Place a pawn in a position of power "},
                {"Obtain an ancient artifact", "Build a construct or magical device", "Carry out a deity's wishes", "Offer sacrifices to a deity ", "Contact a lost deity or power", "Open a gate to another world"},
                {"Fulfill an apocalyptic prophecy", "Enact the vengeful will of a god or patron", "Spread a vile contagion", "Overthrow a government", "Trigger a natural disaster", "Utterly destroy a bloodline or clan"},
                {"Prolong the life of a loved one ", "Prove worthy of another person's love", "Raise or restore a dead loved one", "Destroy rivals for another person's affection "},
                {"Conquer a region or incite a rebellion", "Seize control of an army ", "Become the power behind the throne", "Gain the favor of a ruler"},
                {"Avenge a past humiliation or insult", "Avenge a past imprisonment or injury", "Avenge the death of a loved one", "Retrieve stolen property and punish the thief"},
                {"Control natural resources or trade", "Marry into wealth", "Plunder ancient ruins", "Steal land, goods, or money"}};
        int random_number = random.nextInt(8);
        return schemes[random_number][random.nextInt(schemes[random_number].length)];
    }

    public static String getMethod() {
        Random random = new Random();
        String[][] methods = {
                {"Blight", "Crop failure", "Drought", "Famine "},
                {"Assault or beatings"},
                {"Bounty hunting or assassination"},
                {"Bribery", "Enticement", "Eviction", "Imprisonment", "Kidnapping", "Legal intimidation", "Press gangs", "Shackling", "Slavery", "Threats or harassment"},
                {"Breach of contract", "Cheating", "Fast talking ", "Fine print", "Fraud or swindling", "Quackery or tricks"},
                {"Framing", "Gossiping or slander", "Humiliation", "Libel or insults"},
                {"Dueling"},
                {"Beheading ", "Burning at the stake", " Burying alive", "Crucifixion", "Drawing and quartering", "Hanging", "Impalement", "Sacrifice (living)"},
                {"Impersonation or disguise"},
                {"Lying or perjury"},
                {"Hauntings", "Illusions", "Infernal bargains", "Mind control", "Petrification", "Raising or animating the dead", "Summoning monsters", "Weather control"},
                {"Assassination", "Cannibalism", "Dismemberment", "Drowning", "Electrocution", "Euthanasia (involuntary)", "Disease", "Poisoning", "Stabbing", "Strangulation or suffocation"},
                {"Neglect"},
                {"Betrayal or treason", "Conspiracy", "Espionage or spying", "Genocide", "Oppression", "Raising taxes"},
                {"Curses", "Desecration", "False gods", "Heresy or cults"},
                {"Stalking"},
                {"Arson", "Blackmail or extortion ", "Burglary", "Counterfeiting", "Highway robbery", "Looting ", "Mugging", "Poaching", "Seizing property", "Smuggling"},
                {"Acid", "Blinding", "Branding", "Racking", "Thumbscrews", "Whipping"},
                {"Adultery", "Drugs or alcohol", "Gambling", "Seduction"},
                {"Ambush", "Invasion", "Massacre", "Mercenaries", "Rebellion", "Terrorism"}
        };
        int random_number = random.nextInt(20);
        return methods[random_number][random.nextInt(methods[random_number].length)];
    }

    public static String getWeakness() {
        Random random = new Random();
        String[] weaknesses = {"A hidden object holds the their soul.",
                "Their power is broken if the death of its true love is avenged.",
                "They are weakened in the presence of a particular artifact.",
                "A special weapon deals extra damage when used against them.",
                "They are destroyed if it speaks its true name",
                "An ancient prophecy or riddle reveals how they can be overthrown.",
                "They falls when an ancient enemy forgives its past actions.",
                "They lose their power if a mystic bargain they struck long ago is completed."};
        return weaknesses[random.nextInt(weaknesses.length)];
    }
}
