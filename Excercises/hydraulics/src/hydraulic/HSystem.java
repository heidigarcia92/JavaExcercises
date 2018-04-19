package hydraulic;

import java.util.ArrayList;
import java.util.List;

import hydraulic.exceptions.*;

/**
 * Main class that act as a container of the elements for
 * the simulation of an hydraulics system 
 * 
 */
public class HSystem {
	private final static byte DEFAULT_INITIAL_NUMBER_OF_ELEMENTS = 0x3;

	private Source source;
	private List<Element> elements;
	
	/**
	 * constructor of this class
	 */
	public HSystem() {
		source = null;
		elements = new ArrayList<>(DEFAULT_INITIAL_NUMBER_OF_ELEMENTS);
	}

	/**
	 * Adds a new element to the system
	 * @param elem
	 * @throws MoreThanASourceException if there is already a Source Element 
	 * in the system afert a Source Element is passed as argument
	 */
	public void addElement(Element elem) {
		
		if (elem instanceof Source) {

			if (source != null) {
				throw new MoreThanASourceException();
			}

			source = (Source)elem;
		}

		elements.add(elem);
	}
	
	/**
	 * returns the element added so far to the system
	 * @return an array of elements whose length is equal to the number of added elements
	 */
	public Element[] getElements() {
		
		Element[] elements = new Element[this.elements.size()];
		return this.elements.toArray(elements);
	}
	
	/**
	 * Prints the layout of the system starting at each Source
	 */
	public String layout() {
		StringBuilder string = new StringBuilder();

		getLayoutRecursively(source, source, 0, string);

		return string.toString();
	}
	
	/**
	 * starts the simulation of the system
	 * 
	 * @throws NoSourceException if there is no Source in the System.
	 * @throws NoSinkFoundAtEndOfPathException if there is no Sink at end of a Path
	 */
	public void simulate() {
		
		if (source == null) {
			throw new NoSourceException();
		}

		StringBuilder output = new StringBuilder();

		try {
			recursiveSimulation(source, source.getFlow(), output);
		} catch (NullPointerException e) {
			throw new NoSinkFoundAtEndOfPathException();
		}
		
		System.out.println(output);
	}

	private void recursiveSimulation(Element currentElement, double inputFlow, StringBuilder outputBuffer) {

		StringBuilder string = new StringBuilder()
				.append(currentElement)
				.append('\n');

		if (!(currentElement instanceof Source)) {
			string.append("Input flow: ")
				  .append(inputFlow)
				  .append('\n');
		}

		/* ELEMENT: SOURCE */
		if (currentElement instanceof Source) {
			string.append("Output flow: ")
				  .append(inputFlow)
				  .append('\n');

			outputBuffer.append(string.toString() + "\n");
			recursiveSimulation(currentElement.getOutput(), inputFlow, outputBuffer);
		}

		/* ELEMENT: TAP */
		if (currentElement instanceof Tap) {
			
			Tap tap = (Tap)currentElement;					
			double outputFlow = (tap.isOpen()) ? inputFlow : 0;
		
			string.append("Output flow: ")
			  	  .append(outputFlow)
				  .append('\n');
				  
		    outputBuffer.append(string.toString() + "\n");
			recursiveSimulation(tap.getOutput(), outputFlow, outputBuffer);
		}

		/* ELEMENT: SPLIT */
		if (currentElement instanceof Split) {

			double outputFlow = inputFlow / 2;

			string.append("Output flow: ")
			  	  .append(outputFlow)
				  .append('\n');

			Element[] elements = ((Split)currentElement).getOutputs();
			for (Element e : elements) {
				outputBuffer.append(string.toString() + "\n");
				recursiveSimulation(e, outputFlow, outputBuffer);
			}
		}

		/* ELEMENT: SINK */
		if (currentElement instanceof Sink) {
			outputBuffer.append(string.toString() + "\n");
		}

		/* ELEMENT: NULL */
		if (currentElement == null) {
			throw new NullPointerException();
		}
	}

	private void getLayoutRecursively(Element currentElement, Element prev, int prevLen, StringBuilder layoutBuffer) {

		if (currentElement != source) {
			layoutBuffer.append(' ');

			if (prev instanceof Split) {
				layoutBuffer.append('+');
			}

			layoutBuffer.append("-> ");
		}

		layoutBuffer.append(currentElement);

		if (currentElement instanceof Split) {
			Element[] elements = ((Split)currentElement).getOutputs();

			prevLen += currentElement.toString().length() + 4;
			getLayoutRecursively(elements[0], currentElement, prevLen, layoutBuffer);
			
			layoutBuffer.append('\n');
			for (int i = 0; i <= prevLen; i++) {
				layoutBuffer.append(' ');
			}
			
			layoutBuffer.append("|\n");
			for (int i = 0; i < prevLen; i++) {
				layoutBuffer.append(' ');
			}

			getLayoutRecursively(elements[1], currentElement, prevLen + 3, layoutBuffer);

		} else {
			try {
				getLayoutRecursively(currentElement.getOutput(), currentElement, prevLen + currentElement.toString().length() + 2, layoutBuffer);
			} catch (UnsupportedOperationException e) {
				return;
			} catch (NullPointerException e) {
				return;
			}
		}
	}
}