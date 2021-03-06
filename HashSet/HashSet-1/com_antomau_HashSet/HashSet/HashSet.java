package com_antomau_HashSet.HashSet;

import com_antomau_HashSet.Book.Book;

import java.util.NoSuchElementException;

/**
 * Questa classe deinisce la tabella hash.
 * @author Antonio Maulucci http://www.antomau.com
 */
public class HashSet {
	
	
	
	//definire un nodo della lista
	class Node
	{
		Book aBook;
		Node link;
	}
	
	
	
	
	
	//variabili
	
	private Node[] buckets; //array di nodi
	private int size; //numero di elementi presenti nella struttura dati
	
	
	
	
	
	
	
	/**
	 * Costruttore della classe.
	 * @param bucketsLength Lunghezza dell'array buckets.
	 */
	public HashSet(int bucketsLength)
	{
		buckets = new Node[bucketsLength];
		size = 0;
	}
	
	
	
	
	
	
	
	
	public boolean contains (Book x)
	{
		int h = x.getHashCode();
		
		if (h<0) h*=(-1);
		
		h %= buckets.length;
		
		Node current = buckets[h];
		
		while (current.link != null)
		{
			if (current.aBook.equals(x)) return true;
			current = current.link;
		}
		
		return false;
		
	} //end of contains
	
	
	
	
	
	
	
	/**
	 * Aggiunge un elemento alla struttura.
	 * @param x L'elemento da aggiungere.
	 * @return Vero se l'elemento viene aggiunto, falso se l'elemento gia' esiste e quindi non viene aggiunto.
	 */
	public boolean add(Book x)
	{
		
		int h = x.getHashCode();
		
		if (h<0) h*=(-1);
		
		h%=buckets.length;
		
		Node current = buckets[h];
		
		while (current.link != null)
		{
			if (current.aBook.equals(x)) return false; //se l'elemento gia' esiste e' inutile inserirlo e quindi il metodo restiruisce falso ed interrompe la propria esecuzione
			current = current.link;
		} //end of while
		
		
		Node newNode = new Node();
		
		newNode.aBook = x;
		
		newNode.link = buckets[h]; //questa variabile fa in modo che tutto il contenuto della posizione h dell'array buckets sia il contenuto successivo del nuovo nodo == in poche parole si inserisce il nuovo nodo come testa della lista
		
		buckets[h] = newNode; //la lista aggiornata diviene quindi il contenuto della posizione h dell'array
		
		size++; //incremento della variabile che memorizza quanti elementi ci sono nella struttura
		
		return true;
	}
	
	
	
	
	
	
	
	/**
	 * Elimina un elemento dalla struttura.
	 * @param x L'elemento da eliminare.
	 * @return Vero se viene eliminato, falso se non e' presente e pertanto non viene eliminato.
	 */
	public boolean remove(Book x)
	{
		
		int h = x.getHashCode();
		
		if (h<0) h*=(-1);
		
		h%=buckets.length;
		
		
		Node current = buckets[h];
		Node previous = null;
		
		while (current.link != null)
		{
			if (current.aBook.equals(x)) {
				if (previous == null) buckets[h] = current.link; //se previous e' nullo vuol dire che il nodo da eliminare e' quello in testa alla lista e pertanto la lista contenuta in buckets[h] sara' quella parte che viene dopo il nodo in testa ovverro tutto cio' che ha inizio da "headList".link
				else previous.link = current.link; //si elimina ogni riferimento al nodo corrente poiche' si crea un ponte dal nodo precedente a quello successivo di quello corrente (vedi liste per comprendere meglio)
				size--; //decremento della variabile che memrizza quanti elementi ci sono nella struttura
				return true;
			} //end of if
			previous = current; //scorri la lista
			current = current.link; //scorri la lista
		} //end of while
		
		return false;
	}
	
	
	
	
	
	
	/**
	 * @return Il numero di elementi presenti nella struttura.
	 */
	public int getSize()
	{
		return size;
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * Viene creato un iteratore per la lista
	 * @author Antonio Maulucci http://www.antomau.com
	 *
	 */
	class StructIterator implements HashSetIterator
	{
		//variabili
		private int bucket; //indice dell'array corrente
		private int previousBucket; //indice dell'array precedente
		private Node current; //nodo corrente della lista
		private Node previous; //nodo precedente della lista
		
		
		
		
		/**
		 * Costruttore della classe.
		 */
		public StructIterator()
		{
			this.bucket = -1; //because when you launch program this will be incremented to 0 --> the 1st index of array
			this.previousBucket = -1;
			
			this.current = null;
			this.previous = null;
		}
		
		
		
		
		
		@Override
		public boolean hasNext() {
			if (current != null && current.link != null) return true;
			
			for (int b=bucket+1; b<buckets.length; b++)
			{
				if (buckets[b] != null) return true;
			}
			
			return false;
		}
		
		
		
		
		
		@Override
		public Book next() {
			
			previous = current;
			previousBucket = bucket;
			
			if (current==null || current.link==null) {
				
				bucket++;
				
				while (bucket<buckets.length && buckets[bucket]==null) bucket++;
				
				if (bucket < buckets.length) current=buckets[bucket];
				else throw new NoSuchElementException();
				
			} else
				current = current.link;
			
			return current.aBook;
		}
		
		
		
		
		
		@Override
		public void remove() {
			
			if (previous!=null && previous.link==current) previous.link = current.link;
			else if (previousBucket < bucket) buckets[bucket] = current.link;
			else throw new NoSuchElementException();
			
			//le istruzioni seguenti impediscono due eliminazioni consecutive
			current = previous;
			bucket = previousBucket;
			
		}
		
		
		
		
	} //end of iterator class
	
	
	
	
	
	/**
	 * @return Un iteratore per la lista concatenata.
	 */
	public StructIterator getIterator()
	{
		return new StructIterator();
	}
	
	
	
	
	
	/**
	 * @return A string to set as JLabel text about content of structure.
	 */
	public String toLabelString()
	{
		String result="";
		
		result += "<html><table border=\"1\"><tr><th>Author</th><th>Title</th><th>Year</th></tr>";
		
		StructIterator iterator = new StructIterator();
		
		while (iterator.hasNext()) {
			
			result += "<tr><td>" + iterator.next().getAuthor() + "</td><td>" + iterator.next().getTitle() + "</td><td>" + iterator.next().getYear() + "</td></tr>";
			
		} //end of while
		
		result += "</table></html>";
		
		return result;
	}
	
	
	
} //end of class
