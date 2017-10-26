package BlackJack.model;

import BlackJack.model.rules.*;

public class Dealer extends Player {

  private Deck m_deck;
  private INewGameStrategy m_newGameRule;
  private IHitStrategy m_hitRule;

  public Dealer(RulesFactory a_rulesFactory) {
  
    m_newGameRule = a_rulesFactory.GetNewGameRule();
    m_hitRule = a_rulesFactory.GetHitRule();
    
    /*for(Card c : m_deck.GetCards()) {
      c.Show(true);
      System.out.println("" + c.GetValue() + " of " + c.GetColor());
    }    */
  }
  public void Stand(){
	  if(m_deck != null){
		  super.ShowHand();
		  
		 for(Card hand : super.GetHand()){
			 m_deck.AddCard(hand);
			 hand.Show(true);
		}
		  
	while(m_hitRule.DoHit(this)){
		//maybe
		m_hitRule.DoHit(this);
		
		Card card = m_deck.GetCard(); 
		card.Show(true);
		super.DealCard(card);
	}
	 }
	  
	 
  }
  
  
  public boolean NewGame(Player a_player) {
    if (m_deck == null || IsGameOver()) {
      m_deck = new Deck();
      ClearHand();
      a_player.ClearHand();
      return m_newGameRule.NewGame(m_deck, this, a_player);   
    }
    return false;
  }

  public boolean Hit(Player a_player) {
    if (m_deck != null && a_player.CalcScore() < g_maxScore && !IsGameOver()) {
      Card c;
      c = m_deck.GetCard();
      c.Show(true);
      a_player.DealCard(c);
      
      return true;
    }
    return false;
  }

  public boolean IsDealerWinner(Player a_player) {
	  
	  
    if (a_player.CalcScore() > g_maxScore) {
      return true;
    } else if (CalcScore() > g_maxScore) {
      return false;
    } else  if (a_player.CalcScore() == CalcScore()){    //if the score is equal.
	  return IsEven(a_player);  //this method works as in favour of the dealer. True if dealer wins.
	  }
    return CalcScore() >= a_player.CalcScore(); //make it only  >
  }
  
  /*as i understand if the cards are the same the dealer wins except if the 
   * player has blackjack then the player wins.
   * */
  public boolean IsEven(Player a_player){  
	  int ace = 0;  //count if there is an or and a ten 
	  int ten = 0;
	  int blackjack = 0;    //count how many cards the player has
	  
	  for(Card c : a_player.GetHand()) {
		  if(c.GetValue() == Card.Value.Ace){  //if ace playercards++
			  ace ++;  
		  }
		 if(c.GetValue() == Card.Value.King || c.GetValue() == Card.Value.Ten
		 || c.GetValue() == Card.Value.Knight|| c.GetValue() == Card.Value.Queen){
			 ten ++; //if ten playercards++
		 }
			blackjack ++; //number of players cards ++
		}
	  /*if the player has 2 cards and one of them is a ten and the other is an ace 
	   * then the player wins. In any other circumstances the dealer wins.*/
	  if(ace == 1 && ten == 1 && blackjack ==2)
		  return false;
	  else
		  return true;
	}

  public boolean IsGameOver() {
    if (m_deck != null && m_hitRule.DoHit(this) != true) {
        return true;
    }
    return false;
  }
  
  
}