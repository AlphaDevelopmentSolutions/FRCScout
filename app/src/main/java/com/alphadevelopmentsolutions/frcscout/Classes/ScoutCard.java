package com.alphadevelopmentsolutions.frcscout.Classes;

public class ScoutCard
{

    public static final String TABLE_NAME = "scout_cards";
    public static final String COLUMN_NAME_ID = "Id";
    public static final String COLUMN_NAME_TEAM_ID = "TeamId";
    public static final String COLUMN_NAME_PARTNER_ONE_ID = "PartnerOneId";
    public static final String COLUMN_NAME_PARTNER_TWO_ID = "PartnerTwoId";
    public static final String COLUMN_NAME_ALLIANCE_COLOR = "AllianceColor";
    public static final String COLUMN_NAME_SCORE = "Score";
    public static final String COLUMN_NAME_OPPONENT_SCORE = "OpponentScore";
    public static final String COLUMN_NAME_OPPONENT_ALLIANCE_PARTNER_ONE = "OpponentAlliancePartnerOne";
    public static final String COLUMN_NAME_OPPONENT_ALLIANCE_PARTNER_TWO = "OpponentAlliancePartnerTwo";
    public static final String COLUMN_NAME_OPPONENT_ALLIANCE_PARTNER_THREE = "OpponentAlliancePartnerThree";

    private int id;
    private int teamId;
    private int partnerOneId;
    private int partnerTwoId;
    private AllianceColor allianceColor;
    private int score;
    private int opponentScore;
    private int opponentAlliancePartnerOne;
    private int opponentAlliancePartnerTwo;
    private int opponentAlliancePartnerThree;


    public ScoutCard(
            int id,
            int teamId,
            int partnerOneId,
            int partnerTwoId,
            AllianceColor allianceColor,
            int score,
            int opponentScore,
            int opponentAlliancePartnerOne,
            int opponentAlliancePartnerTwo,
            int opponentAlliancePartnerThree)
    {
        this.id = id;
        this.teamId = teamId;
        this.partnerOneId = partnerOneId;
        this.partnerTwoId = partnerTwoId;
        this.allianceColor = allianceColor;
        this.score = score;
        this.opponentScore = opponentScore;
        this.opponentAlliancePartnerOne = opponentAlliancePartnerOne;
        this.opponentAlliancePartnerTwo = opponentAlliancePartnerTwo;
        this.opponentAlliancePartnerThree = opponentAlliancePartnerThree;
    }

    /**
     * Used for loading
     * @param id to load
     */
    ScoutCard(int id)
    {
        this.id = id;
    }

    //region Getters

    public int getId()
    {
        return id;
    }

    public int getTeamId()
    {
        return teamId;
    }

    public int getPartnerOneId()
    {
        return partnerOneId;
    }

    public int getPartnerTwoId()
    {
        return partnerTwoId;
    }

    public AllianceColor getAllianceColor()
    {
        return allianceColor;
    }

    public int getScore()
    {
        return score;
    }

    public int getOpponentScore()
    {
        return opponentScore;
    }

    public int getOpponentAlliancePartnerOne()
    {
        return opponentAlliancePartnerOne;
    }

    public int getOpponentAlliancePartnerTwo()
    {
        return opponentAlliancePartnerTwo;
    }

    public int getOpponentAlliancePartnerThree()
    {
        return opponentAlliancePartnerThree;
    }


    //endregion

    //region Setters

    public void setId(int id)
    {
        this.id = id;
    }

    public void setTeamId(int teamId)
    {
        this.teamId = teamId;
    }

    public void setPartnerOneId(int partnerOneId)
    {
        this.partnerOneId = partnerOneId;
    }

    public void setPartnerTwoId(int partnerTwoId)
    {
        this.partnerTwoId = partnerTwoId;
    }

    public void setAllianceColor(AllianceColor allianceColor)
    {
        this.allianceColor = allianceColor;
    }

    public void setScore(int score)
    {
        this.score = score;
    }

    public void setOpponentScore(int opponentScore)
    {
        this.opponentScore = opponentScore;
    }

    public void setOpponentAlliancePartnerOne(int opponentAlliancePartnerOne)
    {
        this.opponentAlliancePartnerOne = opponentAlliancePartnerOne;
    }

    public void setOpponentAlliancePartnerTwo(int opponentAlliancePartnerTwo)
    {
        this.opponentAlliancePartnerTwo = opponentAlliancePartnerTwo;
    }

    public void setOpponentAlliancePartnerThree(int opponentAlliancePartnerThree)
    {
        this.opponentAlliancePartnerThree = opponentAlliancePartnerThree;
    }


    //endregion

    //region Load, Save & Delete

    /**
     * Loads the ScoutCard from the database and populates all values
     * @param database used for interacting with the SQLITE db
     * @return boolean if successful
     */
    public boolean load(Database database)
    {
        //try to open the DB if it is not open
        if(!database.isOpen()) database.open();

        if(database.isOpen())
        {
            ScoutCard scoutCard = database.getScoutCard(this);
            database.close();

            if (scoutCard != null)
            {
                setTeamId(scoutCard.getTeamId());
                setPartnerOneId(scoutCard.getPartnerOneId());
                setPartnerTwoId(scoutCard.getPartnerTwoId());
                setAllianceColor(scoutCard.getAllianceColor());
                setScore(scoutCard.getScore());
                setOpponentScore(scoutCard.getOpponentScore());
                setOpponentAlliancePartnerOne(scoutCard.getOpponentAlliancePartnerOne());
                setOpponentAlliancePartnerTwo(scoutCard.getOpponentAlliancePartnerTwo());
                setOpponentAlliancePartnerThree(scoutCard.getOpponentAlliancePartnerThree());
                return true;
            }
        }

        return false;
    }

    /**
     * Saves the ScoutCard into the database
     * @param database used for interacting with the SQLITE db
     * @return int id of the saved ScoutCard
     */
    public int save(Database database)
    {
        int id = -1;

        //try to open the DB if it is not open
        if(!database.isOpen()) database.open();

        if(database.isOpen())
        {
            id = (int) database.setScoutCard(this);
            database.close();
        }

        return id;
    }

    /**
     * Deletes the ScoutCard from the database
     * @param database used for interacting with the SQLITE db
     * @return boolean if successful
     */
    public boolean delete(Database database)
    {
        boolean successful = false;

        //try to open the DB if it is not open
        if(!database.isOpen()) database.open();

        if(database.isOpen())
        {
            successful = database.deleteScoutCard(this);
            database.close();
        }

        return successful;
    }

    //endregion
}
